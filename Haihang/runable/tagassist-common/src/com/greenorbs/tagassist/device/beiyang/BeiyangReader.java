package com.greenorbs.tagassist.device.beiyang;

import java.util.Arrays;

import com.greenorbs.tagassist.ObjectCache;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IReader;
import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.util.HexHelper;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

public class BeiyangReader extends AbstractBeiyangReader implements IReader {
	protected BeiyangReaderDriver _driver;

	private ObjectCache<Observation> _observationCache;

	private int _internalError;

	// Why 200? NO reason.
	public static final int MAX_TRY_READ_TIMES = 200;

	public BeiyangReader() {
		try {
			this._driver = (BeiyangReaderDriver) Native.loadLibrary(
					"BY_RFIDRD600", BeiyangReaderDriver.class);
			_observationCache = new ObjectCache<Observation>(20);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("The reader driver cannot be correctly loaded. " + e);
		}
	}

	public BeiyangReader(String ip, int port) {

		this();

		this._ip = ip;
		this._port = port;

	}

	private void openReader() throws HardwareException {

		_handle = this._driver.BYRD_InitPort(_port, _ip);

		if (this._handle < 0) {
			_log.error("It fails to open the driver of Beiyang reader. The error code is :"
					+ this._handle);

			throw new HardwareException(
					"It fails to start up the Beiyang reader.");
		}

		_log.info("It is successfull to  open the BYRD with port:" + this._port
				+ " and ip:" + this._ip);

	}

	private void closeReader() throws HardwareException {

		if (this._handle < 0) {
			return;
		}

		int rtn = this._driver.BYRD_ClosePort(_handle);

		if (rtn != 0) {
			_log.error("It fails to close the Beiyang reader driver. The error code is: "
					+ String.valueOf(rtn));
			throw new HardwareException("error to close the Beiyang Reader.");
		}

		_log.info("It is successful to close the driver of Beiyang Reader.");

	}

	@Override
	public void startup() throws HardwareException {

		_log.info("The reader is going to startup.");

		if (this._status == IHardware.STATUS_ON) {
			_log.info("The reader is alreaddy on.");
			return;
		}

		super.startup();

		this.openReader();

		_log.info("It is going to start the asynchronous thread for tag identification.");

		this._status = IHardware.STATUS_ON;

		_log.info("The reader is ON.");
	}

	@Override
	public Result shutdown() throws HardwareException {

		if (this._status == IHardware.STATUS_ON) {
			this._status = IHardware.STATUS_OFF;
			this.closeReader();
			super.shutdown();
		}

		return Result.SUCCESS;
	}

	@Override
	public int getReadPointPower(String readPoint) throws HardwareException {

		try {
			byte[] antennaPower = new byte[100];
			int result = this._driver.BYRD_GetAntennaPower(_handle,
					nextCommandCounter(), antennaPower);
			if (result == 0) {

				String str = new String(antennaPower);
				int[] power = new int[4];
				power[0] = Integer.parseInt(str.substring(0, 4));
				power[1] = Integer.parseInt(str.substring(8, 12));
				power[2] = Integer.parseInt(str.substring(16, 20));
				power[3] = Integer.parseInt(str.substring(24, 28));

				int index = Arrays.asList(READ_POINTS).indexOf(readPoint);

				if (index < 0) {
					return -1;
				}

				return power[index];
			}

			throw new HardwareException(
					"Cannot get the read point power from the reader.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new HardwareException(
					"There is an error when get the power of read point.");
		}

	}

	@Override
	public boolean setReadPointPower(String readpoint, int value)
			throws HardwareException {

		try {

			byte[] power = new byte[100];

			int result = this._driver.BYRD_GetAntennaPower(_handle,
					this.nextCommandCounter(), power);

			if (result == 0) {
				power[8 * Integer.parseInt(readpoint)] = (byte) ((value >> 24) & 0xff);
				power[8 * Integer.parseInt(readpoint) + 1] = (byte) ((value >> 16) & 0xff);
				power[8 * Integer.parseInt(readpoint) + 2] = (byte) ((value >> 8) & 0xff);
				power[8 * Integer.parseInt(readpoint) + 3] = (byte) (value & 0xff);
			}
			return this._driver.BYRD_SetAntennaPower(_handle,
					this.nextCommandCounter(), new String(power)) == 0;

		} catch (Exception e) {

			e.printStackTrace();

			throw new HardwareException(
					"There is an error when set the read point power.");
		}

	}

	@Override
	public ObservationReport identify(String[] readPoints)
			throws HardwareException {
		ObservationReport report = new ObservationReport();
		
		_log.debug("start performing identify operation.");
		
		try {

			int result = this._driver.BYRD_SetInventTag6c(_handle,
					this.nextCommandCounter());

			if (result != 0 && result != 100) {
				_log.error("It fails to setInventTag6c. The result code is "
						+ result);
				throw new HardwareException();
			}

			while (true) {
				Memory memory = new Memory(1000);
				ByteByReference info = new ByteByReference();
				info.setPointer(memory);

				IntByReference count = new IntByReference();
				IntByReference finish = new IntByReference();

				result = _driver.BYRD_GetInventTag6cWithRSSI(this._handle,
						info, count, finish);

				if (result != 0 && result != 100) {
					_log.error("It fails to getInventTag6CWithRSSI. The return code is "
							+ result);
					throw new HardwareException();
				}

				if (count.getValue() > 0) {
					// To discovery the tag
					byte[] infoBuffer = new byte[1000];
					info.getPointer().read(0, infoBuffer, 0, infoBuffer.length);
					String data = new String(infoBuffer);
					_log.info("New observation report:[" + data + "]");
					String[] dataArr = data.split("\\%");

					for (int i = 0; i < count.getValue(); i++) {
						if (dataArr[i] == null || dataArr[i].length() < 12) {
							continue;
						}
						String epc = dataArr[i].substring(0,
								dataArr[i].length() - 12);
						int rssi = HexHelper.hexToInt(dataArr[i].substring(
								dataArr[i].length() - 12,
								dataArr[i].length() - 4));
						String readpoint = dataArr[i].substring(
								dataArr[i].length() - 4,
								dataArr[i].length() - 2);
						int c = Integer.valueOf(dataArr[i].substring(
								dataArr[i].length() - 2, dataArr[i].length()));
						Observation observ = new Observation(epc, readpoint,
								rssi, c, System.currentTimeMillis());
						_log.info(observ);

						if (_observationCache.get(epc) == null) {
							_observationCache.put(observ.getEPC(), observ);
							report.add(observ);
						} else {
							_log.info("Frame ignored: duplicated observation.");
						}

					}
				}
				if (finish.getValue() == 1) {
					break;
				}
			}
			
			_log.debug("end performing identify operation.");

			return report;

		} catch (Exception e) {
			e.printStackTrace();
			throw new HardwareException(e);
		}
	}

	ObservationReport parseObsWithRssi(byte[] info) {
		String str = new String(info);
		String[] tags = str.split("\\%");

		ObservationReport report = new ObservationReport();
		for (int i = 0; i < tags.length - 1; i++) {
			Observation obs = new Observation();
			String epc = tags[i].substring(0, tags[i].length() - 8);
			String rssi = tags[i].substring(tags[i].length() - 8,
					tags[i].length() - 4);
			String antenna = tags[i].substring(tags[i].length() - 4,
					tags[i].length() - 2);
			obs.setEPC(epc);
			obs.setRssi(Integer.valueOf(rssi));
			obs.setReadPoint(antenna);
			report.add(obs);
		}

		return report;
	}

	@Override
	public void addIdentifyListener(IdentifyListener l) {

		if (this._asyn == false) {

			this.start();

			this._asyn = true;

		}

		super.addIdentifyListener(l);

	}

	public void run() {

		while (this._status == IHardware.STATUS_ON) {
			if (this._identifyListeners.size() > 0) {
				try {

					ObservationReport report = this.identify(this
							.getReadPoints());
					if (report != null && report.isSucessfull()) {
						this.fireIdentifyEvent(report);
					}
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						// keep nothing
					}
				} catch (HardwareException e) {
					e.printStackTrace();
					_log.error("It fails to conduct the identifiy operation. The system tries to re-open the reader.");

					if (this._internalError++ < MAX_TRY_READ_TIMES) {
						try {
							_log.info("It tries to re-open the reader.");
							this.closeReader();
							this.openReader();
						} catch (Exception e2) {
							_log.error("It fails to try re-open the reader.",
									e2);
							return;
						}
						continue;

					}

				}

			}
		}

	}
}
