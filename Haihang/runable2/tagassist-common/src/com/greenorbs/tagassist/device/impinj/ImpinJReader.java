package com.greenorbs.tagassist.device.impinj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.generated.custom.messages.IMPINJ_ENABLE_EXTENSIONS;
import org.llrp.ltk.generated.custom.messages.IMPINJ_ENABLE_EXTENSIONS_RESPONSE;
import org.llrp.ltk.generated.custom.parameters.ImpinjPeakRSSI;
import org.llrp.ltk.generated.custom.parameters.ImpinjRFPhaseAngle;
import org.llrp.ltk.generated.custom.parameters.ImpinjRFDopplerFrequency;
import org.llrp.ltk.generated.enumerations.GetReaderCapabilitiesRequestedData;
import org.llrp.ltk.generated.enumerations.GetReaderConfigRequestedData;
import org.llrp.ltk.generated.enumerations.StatusCode;
import org.llrp.ltk.generated.messages.ADD_ROSPEC;
import org.llrp.ltk.generated.messages.ADD_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.CLOSE_CONNECTION;
import org.llrp.ltk.generated.messages.CLOSE_CONNECTION_RESPONSE;
import org.llrp.ltk.generated.messages.DELETE_ROSPEC;
import org.llrp.ltk.generated.messages.DELETE_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.ENABLE_ROSPEC;
import org.llrp.ltk.generated.messages.ENABLE_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.GET_READER_CAPABILITIES;
import org.llrp.ltk.generated.messages.GET_READER_CAPABILITIES_RESPONSE;
import org.llrp.ltk.generated.messages.GET_READER_CONFIG;
import org.llrp.ltk.generated.messages.GET_READER_CONFIG_RESPONSE;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;
import org.llrp.ltk.generated.messages.SET_READER_CONFIG;
import org.llrp.ltk.generated.messages.SET_READER_CONFIG_RESPONSE;
import org.llrp.ltk.generated.messages.START_ROSPEC;
import org.llrp.ltk.generated.messages.START_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.STOP_ROSPEC;
import org.llrp.ltk.generated.messages.STOP_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.parameters.AntennaConfiguration;
import org.llrp.ltk.generated.parameters.Custom;
import org.llrp.ltk.generated.parameters.EPC_96;
import org.llrp.ltk.generated.parameters.GeneralDeviceCapabilities;
import org.llrp.ltk.generated.parameters.TagReportData;
import org.llrp.ltk.generated.parameters.TransmitPowerLevelTableEntry;
import org.llrp.ltk.generated.parameters.UHFBandCapabilities;
import org.llrp.ltk.net.LLRPConnection;
import org.llrp.ltk.net.LLRPConnectionAttemptFailedException;
import org.llrp.ltk.net.LLRPConnector;
import org.llrp.ltk.net.LLRPEndpoint;
import org.llrp.ltk.types.LLRPMessage;
import org.llrp.ltk.types.SignedShort;
import org.llrp.ltk.types.UnsignedInteger;
import org.llrp.ltk.types.UnsignedShort;
import org.llrp.ltk.util.Util;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IReaderExt;
import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;

public class ImpinJReader implements IReaderExt, LLRPEndpoint {
	
	private LLRPConnection _connection;

	protected static Logger _logger = Logger.getLogger(ImpinJReader.class);

	protected IdentifyListener _identifyListener;

	private int _messageID = 2;

	private String _ip;

	// default port;
	private int _port = 5084;

	private String _roSpecFile;

	private String _readerConfigFile;

	private ObservationReport _report = new ObservationReport();

	ADD_ROSPEC roSpec;

	public static final int POWER91 = 91;
	public static final int POWER87 = 87;
	public static final int POWER61 = 61;
	public static final int POWER41 = 41;
	public static final int POWER35 = 35;

	private int _status = 0;

	public ImpinJReader() {
		this("localhost");
	}

	public ImpinJReader(String ip, int port) {

		this._ip = ip;
		this._port = port;
		this._status = IHardware.STATUS_IDLE;

	}

	/**
	 * default port 5084
	 * 
	 * @param ip
	 */
	public ImpinJReader(String ip) {
		this(ip, 5084);
	}

	public String getRoSpecFile() {
		return _roSpecFile;
	}

	public void setRoSpecFile(String roSpecFile) {
		_roSpecFile = roSpecFile;
	}

	public String getReaderConfigFile() {
		return _readerConfigFile;
	}

	public void setReaderConfigFile(String readerConfigFile) {
		_readerConfigFile = readerConfigFile;
	}

	private void connect() throws LLRPConnectionAttemptFailedException {
		try {

			if (this._connection == null) {
				this._connection = new LLRPConnector(this, this.getIP(),
						this.getPort());
			}
			
			System.out.println(this.getIP()+":"+this.getPort());

			_logger.info("Initiate LLRP connection to reader");
			((LLRPConnector) _connection).connect();
			this._status = IHardware.STATUS_ON;
		} catch (LLRPConnectionAttemptFailedException ex) {
			ex.printStackTrace();
			_logger.error("it fails to connect the readre");
			this._status = IHardware.STATUS_ERROR;
			throw ex;
		}
	}

	private void disconnect() throws InvalidLLRPMessageException,
			TimeoutException {
		LLRPMessage response;
		CLOSE_CONNECTION close = new CLOSE_CONNECTION();
		close.setMessageID(getUniqueMessageID());
		try {
			// don't wait around too long for close
			response = this._connection.transact(close, 4000);

			// check whether ROSpec addition was successful
			StatusCode status = ((CLOSE_CONNECTION_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("CLOSE_CONNECTION was successful");
				this._status = IHardware.STATUS_ON;
			} else {
				_logger.info(response.toXMLString());
				_logger.info("CLOSE_CONNECTION Failed ... continuing anyway");
				this._status = IHardware.STATUS_OFF;
			}
		} catch (InvalidLLRPMessageException ex) {
			_logger.error("CLOSE_CONNECTION: Received invalid response message");
			this._status = IHardware.STATUS_ERROR;
			throw ex;
		} catch (TimeoutException ex) {
			_logger.info("CLOSE_CONNECTION Timeouts ... continuing anyway");
			this._status = IHardware.STATUS_ERROR;
			throw ex;
		}

	}

	public void enable(UnsignedInteger roSpecId) {
		LLRPMessage response;
		try {
			// factory default the reader
			_logger.info("ENABLE_ROSPEC ...");
			ENABLE_ROSPEC ena = new ENABLE_ROSPEC();
			ena.setMessageID(getUniqueMessageID());
			ena.setROSpecID(roSpecId);

			response = _connection.transact(ena, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((ENABLE_ROSPEC_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("ENABLE_ROSPEC was successful");
			} else {
				_logger.error(response.toXMLString());
				_logger.info("ENABLE_ROSPEC_RESPONSE failed ");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void stop(UnsignedInteger rospecId) {
		LLRPMessage response;
		try {
			_logger.info("STOP_ROSPEC ...");
			STOP_ROSPEC stop = new STOP_ROSPEC();
			stop.setMessageID(getUniqueMessageID());
			stop.setROSpecID(rospecId);

			response = _connection.transact(stop, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((STOP_ROSPEC_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("STOP_ROSPEC was successful");
			} else {
				_logger.error(response.toXMLString());
				_logger.info("STOP_ROSPEC_RESPONSE failed ");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void enableImpinjExtensions() {
		LLRPMessage response;

		try {
			_logger.info("IMPINJ_ENABLE_EXTENSIONS ...");
			IMPINJ_ENABLE_EXTENSIONS ena = new IMPINJ_ENABLE_EXTENSIONS();
			ena.setMessageID(getUniqueMessageID());

			response = _connection.transact(ena, 10000);

			StatusCode status = ((IMPINJ_ENABLE_EXTENSIONS_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("IMPINJ_ENABLE_EXTENSIONS was successful");
			} else {
				_logger.info(response.toXMLString());
				_logger.info("IMPINJ_ENABLE_EXTENSIONS Failure");
				System.exit(1);
			}
		} catch (InvalidLLRPMessageException ex) {
			_logger.error("Could not process IMPINJ_ENABLE_EXTENSIONS response");
		} catch (TimeoutException ex) {
			_logger.error("Timeout Waiting for IMPINJ_ENABLE_EXTENSIONS response");
		}
	}

	@Override
	public void startup() throws HardwareException {
		try {

			this.connect();
			this.enableImpinjExtensions();

			this.deleteAllRoSpec();
			SET_READER_CONFIG readerConfig = loadReaderConfig(this
					.getReaderConfigFile());
			this.setReaderConfiguration(readerConfig);
			roSpec = loadROSpec(this.getRoSpecFile());
			this.sendRoSpec(roSpec);
			this.enable(roSpec.getROSpec().getROSpecID());
			this.startRospec(roSpec.getROSpec().getROSpecID());
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error("it fails to startup the impinjReader.", e);
			throw new HardwareException(e);
		}

	}

	@Override
	public Result shutdown() throws HardwareException {

		try {
			this.stopRoSpec(roSpec.getROSpec().getROSpecID());
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(Result.CODE_FAILURE, e.toString());
		}

		return new Result(Result.CODE_SUCCESS);
	}

	private UnsignedInteger getUniqueMessageID() {
		return new UnsignedInteger(_messageID++);
	}

	@Override
	public Result reset() throws HardwareException {

		try {
			disconnect();
			connect();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(Result.CODE_FAILURE, e.toString());
		}

		return Result.SUCCESS;
	}

	/**
	 * 0 - ready 1 - connected 2 - disconnected 3 - error
	 */
	@Override
	public int getStatus() {
		return this._status;
	}

	@Override
	public void setIP(String ip) {
		this._ip = ip;
	}

	@Override
	public String getIP() {
		return this._ip;
	}

	@Override
	public void setPort(int port) {
		this._port = port;
	}

	@Override
	public int getPort() {
		return this._port;
	}

	public void setIdentifyListener(IdentifyListener listener) {
		this._identifyListener = listener;
	}

	@Override
	public IdentifyListener getIdentifyListener() {
		return this._identifyListener;
	}

	@Override
	public void errorOccured(String arg0) {
		_logger.error(arg0);

	}

	@Override
	public synchronized void messageReceived(LLRPMessage message) {

		if (message.getTypeNum() == RO_ACCESS_REPORT.TYPENUM) {

			// The message received is an Access Report.
			RO_ACCESS_REPORT report = (RO_ACCESS_REPORT) message;

			this._report.clear();
			// Get a list of the tags read.
			List<TagReportData> tags = report.getTagReportDataList();
			// Loop through the list and get the EPC of each tag.
			for (TagReportData tag : tags) {

				Observation obs = new Observation();

				obs.setEPC(((EPC_96) tag.getEPCParameter()).getEPC().toString()
						.toUpperCase());

				if (tag.getAntennaID() != null) {
					obs.setReadPoint(tag.getAntennaID().getAntennaID()
							.toString());
				}

				if (tag.getPeakRSSI() != null) {
					obs.setRssi(tag.getPeakRSSI().getPeakRSSI().intValue());
				}

				if (tag.getChannelIndex() != null) {
					obs.setChannelIndex(tag.getChannelIndex().getChannelIndex()
							.intValue());
				}

				if (tag.getFirstSeenTimestampUTC() != null) {
					obs.setTimeStamp(System.currentTimeMillis());
				}

				if (tag.getTagSeenCount() != null) {
					obs.setCount(tag.getTagSeenCount().getTagCount().intValue());
				}
				List<Custom> clist = tag.getCustomList();

				for (Custom cd : clist) {
					if (cd.getClass() == ImpinjRFPhaseAngle.class) {
						obs.setPhaseAngle(((ImpinjRFPhaseAngle) cd)
								.getPhaseAngle().toInteger());
					}
					if (cd.getClass() == ImpinjPeakRSSI.class) {
						obs.setPeekRssi(((ImpinjPeakRSSI) cd).getRSSI()
								.intValue());
					}

					if (cd.getClass() == ImpinjRFDopplerFrequency.class) {
						obs.setDopperFrequency(((ImpinjRFDopplerFrequency) cd)
								.getDopplerFrequency().intValue());
					}

				}
				
				_logger.info(obs);

				this._report.add(obs);

				if (this._identifyListener != null) {
					try {
						this._identifyListener.identifyPerformed(_report);
					} catch (Exception e) {
						e.printStackTrace();
						_logger.error("fail to revoke the IdentifyListener", e);
					}
				}
			}

		}
	}

	public void getReaderCapabilities() {
		LLRPMessage response;
		GET_READER_CAPABILITIES_RESPONSE gresp;

		GET_READER_CAPABILITIES get = new GET_READER_CAPABILITIES();
		GetReaderCapabilitiesRequestedData data = new GetReaderCapabilitiesRequestedData(
				GetReaderCapabilitiesRequestedData.All);
		get.setRequestedData(data);
		get.setMessageID(getUniqueMessageID());
		_logger.info("Sending GET_READER_CAPABILITIES message  ...");
		try {
			response = this._connection.transact(get, 10000);

			// check whether GET_CAPABILITIES addition was successful
			gresp = (GET_READER_CAPABILITIES_RESPONSE) response;
			StatusCode status = gresp.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("GET_READER_CAPABILITIES was successful");

				// get the info we need
				GeneralDeviceCapabilities dev_cap = gresp
						.getGeneralDeviceCapabilities();
				if ((dev_cap == null)
						|| (!dev_cap.getDeviceManufacturerName().equals(
								new UnsignedInteger(25882)))) {
					_logger.error("DocSample4 must use Impinj model Reader, not "
							+ dev_cap.getDeviceManufacturerName().toString());
				}

				UnsignedInteger modelName = dev_cap.getModelName();
				_logger.info("Found Impinj reader model "
						+ modelName.toString());

				// get the max power level
				if (gresp.getRegulatoryCapabilities() != null) {
					UHFBandCapabilities band_cap = gresp
							.getRegulatoryCapabilities()
							.getUHFBandCapabilities();

					List<TransmitPowerLevelTableEntry> pwr_list = band_cap
							.getTransmitPowerLevelTableEntryList();

					TransmitPowerLevelTableEntry entry = pwr_list.get(pwr_list
							.size() - 1);

					UnsignedShort maxPowerIndex = entry.getIndex();
					SignedShort maxPower = entry.getTransmitPowerValue();
					// LLRP sends power in dBm * 100
					double d = ((double) maxPower.intValue()) / 100;

					_logger.info("Max power " + d + " dBm at index "
							+ maxPowerIndex.toString());
				}
			} else {
				_logger.info(response.toXMLString());
				_logger.info("GET_READER_CAPABILITIES failures");
			}
		} catch (InvalidLLRPMessageException ex) {
			_logger.error("Could not display response string");
		} catch (TimeoutException ex) {
			_logger.error("Timeout waiting for GET_READER_CAPABILITIES response");
		}
	}

	public void getReaderConfiguration() {
		LLRPMessage response;
		GET_READER_CONFIG_RESPONSE gresp;

		GET_READER_CONFIG get = new GET_READER_CONFIG();
		GetReaderConfigRequestedData data = new GetReaderConfigRequestedData(
				GetReaderConfigRequestedData.All);
		get.setRequestedData(data);
		get.setMessageID(getUniqueMessageID());
		get.setAntennaID(new UnsignedShort(0));
		get.setGPIPortNum(new UnsignedShort(0));
		get.setGPOPortNum(new UnsignedShort(0));

		_logger.info("Sending GET_READER_CONFIG message  ...");
		try {
			response = this._connection.transact(get, 10000);

			// check whether GET_CAPABILITIES addition was successful
			gresp = (GET_READER_CONFIG_RESPONSE) response;
			StatusCode status = gresp.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("GET_READER_CONFIG was successful");

				List<AntennaConfiguration> alist = gresp
						.getAntennaConfigurationList();

				if (!alist.isEmpty()) {
					AntennaConfiguration a_cfg = alist.get(0);
					UnsignedShort channelIndex = a_cfg.getRFTransmitter()
							.getChannelIndex();
					UnsignedShort hopTableID = a_cfg.getRFTransmitter()
							.getHopTableID();
					_logger.info("ChannelIndex " + channelIndex.toString()
							+ " hopTableID " + hopTableID.toString());
				} else {
					_logger.error("Could not find antenna configuration");
				}
			} else {
				_logger.info(response.toXMLString());
				_logger.info("GET_READER_CONFIG failures");
			}
		} catch (InvalidLLRPMessageException ex) {
			_logger.error("Could not display response string");
		} catch (TimeoutException ex) {
			_logger.error("Timeout waiting for GET_READER_CONFIG response");
		}
	}

	public void setReaderConfiguration(SET_READER_CONFIG setReaderConfig) {
		LLRPMessage response;

		_logger.info("Loading SET_READER_CONFIG message from file SET_READER_CONFIG.xml ...");

		try {

			response = this._connection.transact(setReaderConfig, 10000);

			// check whetherSET_READER_CONFIG addition was successful
			StatusCode status = ((SET_READER_CONFIG_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("SET_READER_CONFIG was successful");
			} else {
				_logger.info(response.toXMLString());
				_logger.info("SET_READER_CONFIG failures");
			}

		} catch (TimeoutException ex) {
			_logger.error("Timeout waiting for SET_READER_CONFIG response");

		} catch (InvalidLLRPMessageException ex) {
			_logger.error("Unable to convert LTK-XML to Internal Object");
		}
	}

	public void sendRoSpec(ADD_ROSPEC addRospec) {
		LLRPMessage response;

		addRospec.setMessageID(getUniqueMessageID());
		// ROSpec rospec = addRospec.getROSpec();

		_logger.info("Sending ADD_ROSPEC message  ...");
		try {
			response = this._connection.transact(addRospec, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((ADD_ROSPEC_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("ADD_ROSPEC was successful");
			} else {
				_logger.info(response.toXMLString());
				_logger.info("ADD_ROSPEC failures");
			}
		} catch (InvalidLLRPMessageException ex) {
			_logger.error("Could not display response string");
		} catch (TimeoutException ex) {
			_logger.error("Timeout waiting for ADD_ROSPEC response");
		}
	}

	public void enableRoSpec(UnsignedInteger roSpecId) {
		LLRPMessage response;
		try {
			// factory default the reader
			_logger.info("ENABLE_ROSPEC ...");
			ENABLE_ROSPEC ena = new ENABLE_ROSPEC();
			ena.setMessageID(getUniqueMessageID());
			ena.setROSpecID(roSpecId);

			response = this._connection.transact(ena, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((ENABLE_ROSPEC_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("ENABLE_ROSPEC was successful");
			} else {
				_logger.error(response.toXMLString());
				_logger.info("ENABLE_ROSPEC_RESPONSE failed ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void startRospec(UnsignedInteger roSpecId) {
		LLRPMessage response;
		try {
			_logger.info("START_ROSPEC ...");
			START_ROSPEC start = new START_ROSPEC();
			start.setMessageID(getUniqueMessageID());
			start.setROSpecID(roSpecId);

			response = this._connection.transact(start, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((START_ROSPEC_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("START_ROSPEC was successful");
			} else {
				_logger.error(response.toXMLString());
				_logger.info("START_ROSPEC_RESPONSE failed ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void stopRoSpec(UnsignedInteger rospecId) {
		LLRPMessage response;
		try {
			_logger.info("STOP_ROSPEC ...");
			STOP_ROSPEC stop = new STOP_ROSPEC();
			stop.setMessageID(getUniqueMessageID());
			stop.setROSpecID(rospecId);

			response = this._connection.transact(stop, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((STOP_ROSPEC_RESPONSE) response)
					.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				_logger.info("STOP_ROSPEC was successful");
			} else {
				_logger.error(response.toXMLString());
				_logger.info("STOP_ROSPEC_RESPONSE failed ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error("fail to stop the ro spec", e);
		}
	}

	public void deleteRoSpec(int RoSpecId) {
		DELETE_ROSPEC_RESPONSE response;

		System.out.println("Deleting all ROSpecs.");
		DELETE_ROSPEC del = new DELETE_ROSPEC();
		// Use zero as the ROSpec ID.
		// This means delete all ROSpecs.
		del.setROSpecID(new UnsignedInteger(RoSpecId));
		try {
			response = (DELETE_ROSPEC_RESPONSE) this._connection.transact(del,
					10000);
			System.out.println(response.toXMLString());
		} catch (Exception e) {
			System.out.println("Error deleting ROSpec.");
			e.printStackTrace();
		}
	}

	/**
	 * Delete all RoSpecs form the reader
	 * 
	 * @param roSpect
	 */
	public void deleteAllRoSpec() {
		this.deleteRoSpec(0);
	}

	public static ADD_ROSPEC loadROSpec(String roSpecFile) {
		_logger.info("Loading ADD_ROSPEC message from file ADD_ROSPEC.xml ...");
		try {
			LLRPMessage addRospec = Util
					.loadXMLLLRPMessage(new File(roSpecFile));
			return (ADD_ROSPEC) addRospec;
		} catch (FileNotFoundException ex) {
			_logger.error("Could not find file");
		} catch (IOException ex) {
			_logger.error("IO Exception on file");
		} catch (JDOMException ex) {
			_logger.error("Unable to convert LTK-XML to DOM");
		} catch (InvalidLLRPMessageException ex) {
			_logger.error("Unable to convert LTK-XML to Internal Object");
		}
		return null;
	}

	public static SET_READER_CONFIG loadReaderConfig(String readerConfigFile) {
		try {
			LLRPMessage setConfigMsg = Util.loadXMLLLRPMessage(new File(
					readerConfigFile));
			return (SET_READER_CONFIG) setConfigMsg;
		} catch (FileNotFoundException ex) {
			_logger.error("Could not find file");
		} catch (IOException ex) {
			_logger.error("IO Exception on file");
		} catch (JDOMException ex) {
			_logger.error("Unable to convert LTK-XML to DOM");
		} catch (InvalidLLRPMessageException ex) {
			_logger.error("Unable to convert LTK-XML to Internal Object");
		}
		return null;
	}

}
