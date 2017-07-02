package com.greenorbs.tagassist.device.beiyang2;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.BarcodeScanListener;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IBarcodeScanner;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.ISerialReader;
import com.greenorbs.tagassist.device.SerialComm;

public class BeiyangBarcodeScanner implements IBarcodeScanner, ISerialReader {

	private Logger _log = Logger.getLogger(this.getClass());
	private int _status;
	private BarcodeScanListener _listener;
	private SerialComm _comm;
	private String _serialPort = "COM3";
	private int _baudRate = 9600;

	@Override
	public void startup() throws HardwareException {
		_log.info("Beiyang Barcode Scanner is starting up...");
		try {
			_comm = new SerialComm(_serialPort, getBaudRate(), this, true);
			_status = IHardware.STATUS_ON;
			_log.info("Beiyang Barcode Scanner started");
		} catch (Exception ex) {
			ex.printStackTrace();
			_status = IHardware.STATUS_ERROR;
			_log.error("Beiyang Barcode Error!", ex);
			throw new HardwareException("startup error.", ex);
		}
	}

	@Override
	public Result shutdown() throws HardwareException {
		_comm.close();
		_status = IHardware.STATUS_OFF;
		_log.info("Beiyang Barcode Scanner stopped");
		return Result.SUCCESS;
	}

	@Override
	public Result reset() throws HardwareException {
		_comm.close();
		startup();
		if (_status == IHardware.STATUS_ON) {
			return Result.SUCCESS;
		}
		return Result.FAILURE;
	}

	@Override
	public int getStatus() {
		return _status;
	}

	@Override
	public void setScanListener(BarcodeScanListener listener) {
		this._listener = listener;
	}

	@Override
	public BarcodeScanListener getScanListener() {
		return this._listener;
	}

	@Override
	public void process(byte[] data) {
		String barcode = new String(data);
		_log.info("Get barcode: " + barcode);
		if (_listener != null) {
			_listener.barcodeScanned(barcode);
		}
	}

	public String getSerialPort() {
		return _serialPort;
	}

	public void setSerialPort(String serialPort) {
		_serialPort = serialPort;
	}

	public int getBaudRate() {
		return _baudRate;
	}

	public void setBaudRate(int baudRate) {
		_baudRate = baudRate;
	}

	
}
