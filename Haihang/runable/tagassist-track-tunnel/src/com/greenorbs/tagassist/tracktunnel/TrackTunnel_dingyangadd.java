/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Longfei Shangguan, Feb 6, 2012
 */

package com.greenorbs.tagassist.tracktunnel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.cfg.Settings;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.ICautionLight;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IReaderExt;
import com.greenorbs.tagassist.device.IStatusLight;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.device.impinj_dingyangadd.SampleProperties;
import com.greenorbs.tagassist.device.impinj_dingyangadd.TagOpCompleteListenerImplementation;
import com.greenorbs.tagassist.epc.EPC;
import com.greenorbs.tagassist.epc.EPC96;
import com.greenorbs.tagassist.epc.EPC_DY;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FlightTerminate;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.TrackTunnelConfig;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageArrival;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightScheduled;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Heartbeat;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.TrackTunnelInfoReport;
import com.greenorbs.tagassist.messagebus.util.AbstractHardware;
import com.greenorbs.tagassist.messagebus.util.cache.BaggageCache;
import com.greenorbs.tagassist.messagebus.util.cache.FlightCache;
import com.greenorbs.tagassist.util.UserBankData;
import com.impinj.octane.*;

public class TrackTunnel_dingyangadd extends AbstractHardware implements IdentifyListener {

	private static Logger _logger = Logger.getLogger(TrackTunnel_dingyangadd.class);

	private FlightCache _flightCache;
	private BaggageCache _baggageCache;

	private static SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	private static SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	private static String[] MONTH = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
			"DEC" };

	private String _uuid;
	private String _name;
	private String _hostname;
	private String _ownerPool;
	private float _distance;
	// private Conveyor _conveyor;

	private TrackTunnelInfo _trackTunnelInfo;

	private ImpinjReader _reader_dy;
	// private IStatusLight _statusLight;
	// private ICautionLight _cautionLight;
	private int _status;

	private static final long PUBLISH_MSG_BLINK_INTERVAL = 200l;
	private static final long OBSERVATION_BLINK_INTERVAL = 50l;

	private boolean _publishBaggageTracked;
	private boolean _publishBaggageArrival;
	private boolean _simuMode;
	private String _resetCommand;
	private ExecutorService _cachedThreadPool = Executors.newCachedThreadPool();

	public void setPublishBaggageTracked(boolean _publishBaggageTracked) {
		this._publishBaggageTracked = _publishBaggageTracked;
	}

	public void setPublishBaggageArrival(boolean _publishBaggageArrival) {
		this._publishBaggageArrival = _publishBaggageArrival;
	}

	public void setSimuMode(boolean _simuMode) {
		this._simuMode = _simuMode;
	}

	public void setResetCommand(String _resetCommand) {
		this._resetCommand = _resetCommand;
	}

	public TrackTunnel_dingyangadd() {
		super();
		_trackTunnelInfo = new TrackTunnelInfo();
		_flightCache = new FlightCache(_publisher);
		_baggageCache = new BaggageCache(_publisher);
		_reader_dy = new ImpinjReader();
		this._status = IHardware.STATUS_OFF;
	}

	@Override
	protected void initSubscriber() {
		super.initSubscriber();
		this.subscribe(TrackTunnelConfig.class);
		this.subscribe(BaggageArrival.class);
		this.subscribe(FlightScheduled.class);
		this.subscribe(FlightTerminate.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;

		if (message instanceof TrackTunnelConfig) {

			TrackTunnelConfig m = (TrackTunnelConfig) message;
			String targetUUID = m.getTrackTunnelId();

			if (this.getUUID().equals(targetUUID)) {

				this.setName(m.getName());
				this.setOwnerPool(m.getPoolId());
				this.setDistance(m.getDistance());

				reply = new Confirmation(m.getMessageId(), Confirmation.RESULT_SUCCESS);
			}

		} else if (message instanceof BaggageArrival) {

			BaggageArrival m = (BaggageArrival) message;
			final BaggageInfo baggageInfo = m.getBaggageInfo();

			_baggageCache.put(baggageInfo);

			// dingyang: set false in general(bean_test.xml).
			if (_simuMode) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
//						TrackTunnel_dingyangadd.this.publishBaggageTracked(baggageInfo.getEPC(), -50.0f);
					}
				}, 1000);
			}

		} else if (message instanceof FlightScheduled) {

			FlightScheduled m = (FlightScheduled) message;
			FlightInfo flightInfo = m.getFlightInfo();

			_flightCache.put(flightInfo);

		} else if (message instanceof FlightTerminate) {

			FlightTerminate m = (FlightTerminate) message;
			String flightId = m.getFlightId();

			_flightCache.remove(flightId);
			_baggageCache.clear(flightId);

		} else {
			return super.handleMessage(message);
		}

		return reply;
	}

	@Override
	public void startup() throws HardwareException, OctaneSdkException {
		_logger.info("Starting up...");
		super.startup();

		_trackTunnelInfo.setUUID(this.getUUID());
		_trackTunnelInfo.setLocationParam1(this.getOwnerPool());
		_trackTunnelInfo.setLocationParam2(this.getDistance());

		// init cache immediately
		_flightCache.initialize();
		_baggageCache.initialize(_flightCache.flights());
		_logger.info("Cache initialized.");

		// dingyang add
		// start up reader
		if (_reader_dy != null) {
			// Connect
			_logger.info("Connecting to " + _hostname);
			_reader_dy.connect(_hostname);

			// Get the default settings
			com.impinj.octane.Settings settings = _reader_dy.queryDefaultSettings();
			settings.getReport().setIncludeAntennaPortNumber(true);

			// Apply the new settings
			_reader_dy.applySettings(settings);

			// create the reader op sequence
			TagOpSequence seq = new TagOpSequence();
			seq.setOps(new ArrayList<TagOp>());
			seq.setExecutionCount((short) 0); // forever
			seq.setState(SequenceState.Active);
			seq.setId(1);

			// create an reader op
			TagReadOp readOp = new TagReadOp();
			readOp.setMemoryBank(MemoryBank.User);
			readOp.setWordCount((short) 32);
			readOp.setWordPointer((short) 0);

			// add to the list
			seq.getOps().add(readOp);
			// set target tag's EPC
			String targetEpc = System.getProperty(SampleProperties.targetTag);
			_logger.info("targetEPC:" + targetEpc);

			// set what to read from tag
			if (targetEpc != null) {
				seq.setTargetTag(new TargetTag());
				seq.getTargetTag().setBitPointer(BitPointers.Epc);
				seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
				seq.getTargetTag().setMemoryBank(MemoryBank.User);
				seq.getTargetTag().setData(targetEpc);
			} else {
				// or just send NULL to apply to all tags
				seq.setTargetTag(null);

			}

			// add to the reader. The reader supports multiple sequences
			_reader_dy.addOpSequence(seq);
			// set up listeners to hear stuff back from SDK. Normally
			// you would turn on a tag report listener, but we will keep
			// it disabled here so we can see what is going on
			TagOpCompleteListenerImplementation TOC_Imp = new TagOpCompleteListenerImplementation();
			TOC_Imp.setIndentifyListener(this);
			_reader_dy.setTagOpCompleteListener(TOC_Imp);

			// ((IReaderExt) _reader_dy).setIdentifyListener(this);
			// Start the reader
			_reader_dy.start();

		} else {
			_logger.warn("Reader not found.");
		}

		this._status = IHardware.STATUS_ON;
		this.publishHardwareInfoReport();
		_logger.info("Reader started up.");
		System.out.println("Press Enter to exit.");
		Scanner s = new Scanner(System.in);
		s.nextLine();
		System.out.println("Stopping  " + _hostname);
		_reader_dy.stop();
		System.out.println("Disconnecting from " + _hostname);
		_reader_dy.disconnect();
		s.close();
		System.out.println("Done");

	}

	@Override
	public Result shutdown() throws HardwareException {
		_logger.info("Shutting down...");

		// shut down the reader
		if (_reader_dy != null) {
			try {
				_reader_dy.stop();
			} catch (OctaneSdkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_logger.info("Reader shut down.");
		}

		// // shut down the status light
		// if (_statusLight != null) {
		// _statusLight.shutdown();
		// _statusLight.turnOff(IStatusLight.YELLOW);
		// _logger.info("Status light shut down.");
		// }
		//
		// // shut down the caution light
		// if (_cautionLight != null) {
		// _cautionLight.shutdown();
		// _cautionLight.turnOff(ICautionLight.BLACK);
		// _logger.info("Caution light shut down.");
		// }

		this._status = IHardware.STATUS_OFF;
		this.publishHardwareInfoReport();

		return super.shutdown();
	}

	@Override
	public Result reset() throws HardwareException {

		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File("tracktunnel.jar");

		/* is it a jar file? */
		if (!currentJar.exists() || !currentJar.getName().endsWith(".jar"))
			return Result.FAILURE;

		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());
		command.add("track tunnel restart successfully");

		// final ProcessBuilder builder = new ProcessBuilder(command);
		final ProcessBuilder builder = new ProcessBuilder(_resetCommand);

		try {
			_logger.info("track tunnel is restarting");
			builder.start();
		} catch (IOException e) {
			_logger.error("track tunnel restart failed");
			return Result.FAILURE;
		}

		System.exit(0);
		return Result.SUCCESS;
	}

	@Override
	public void identifyPerformed(ObservationReport report) {
		if (this.getStatus() == IHardware.STATUS_ON) {
			for (Observation observation : report) {
				this.handleObservation(observation);
			}
		}
	}

	private void handleObservation(Observation observation) {
		EPC_DY epc = null;
		try {
			epc = new EPC_DY(observation.getEPC());
		} catch (Exception e) {
			_logger.error("Invalid epc encountered: " + observation.getEPC());
			return;
		}

		if (epc != null) {
			UserBankData userBankData = new UserBankData(observation.getData());
			if (this._publishBaggageArrival) {

				this.publishBaggageArrival(epc, userBankData);
			}
			if (this._publishBaggageTracked) {
				// _cachedThreadPool.execute(new Runnable() {
				// @Override
				// public void run() {
				// blinkStatusLight(IStatusLight.YELLOW,
				// OBSERVATION_BLINK_INTERVAL);
				// }
				// });
				this.publishBaggageTracked(epc, observation);
			}
		}
	}

	private void publishBaggageArrival(EPC_DY epc, UserBankData userBankData) {
		
		String baggageNumber = epc.getBaggageNumber();
		if (baggageNumber == null || _baggageCache.contains(baggageNumber)) {
			return;
		} else {
			_baggageCache.update(baggageNumber);
			if (_baggageCache.contains(baggageNumber)) {
				return;
			}
		}

		// dingyang add
		String flightCode = userBankData.getFlightcode();
		// String flightCode = epc.getFlightCode();
		if (flightCode == null) {
			System.out.println("flihtCode is empty!");
			return;
		}

		long time = System.currentTimeMillis();
		String day = dayFormat.format(time);
		String month = MONTH[Integer.parseInt(monthFormat.format(time)) - 1];
		String year = yearFormat.format(time);

		String flightId = String.format("%s/%s%s%s", flightCode, day, month, year);

		if (_flightCache.contains(flightId)) {
			BaggageInfo baggage = new BaggageInfo();
			baggage.setNumber(baggageNumber);
			baggage.setEPC(epc.toString());
			baggage.setFlightId(flightId);

			// publish message
			BaggageArrival arrival = new BaggageArrival();
			arrival.setBaggageInfo(baggage);
			this.publish(arrival);
		}
	}

	private void publishBaggageTracked(EPC_DY epc, Observation observation) {
		BaggageTracked tracked = new BaggageTracked();
		tracked.setEPC(epc.getHex());
		tracked.setPoolId(this.getOwnerPool());
		tracked.setDistance(this.getDistance());
		tracked.setRssi((float) observation.getRssi());
		// publish message
		this.publish(tracked);

	}

	@Override
	protected void customizeHeartbeat(Heartbeat heartbeat) {
		heartbeat.setCustom(_trackTunnelInfo);
	}

	@Override
	protected HardwareInfoReport generateHardwareInfoReport() {
		TrackTunnelInfoReport report = new TrackTunnelInfoReport();

		report.setName(this.getName());
		report.setStatus(this.getStatus());
		report.setSoftwareVersion(this.getSoftwareVersion());
		report.setHostname(this.getHostname());
		report.setMacAddress(this.getMacAddress());
		report.setIpAddress(this.getIpAddress());
		report.setLocationParam1(this.getOwnerPool());
		report.setLocationParam2(this.getDistance());

		return report;
	}

	@Override
	public int getComponent() {
		return Identifiable.COMPONENT_TRACK_TUNNEL;
	}

	@Override
	public String getUUID() {
		return this._uuid;
	}

	public void setUUID(String uuid) {
		_uuid = uuid;
	}

	@Override
	public String getHostname() {
		return this._hostname;
	}

	public void sethostname(String hostname) {
		_hostname = hostname;
	}

	@Override
	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getOwnerPool() {
		return _ownerPool;
	}

	public void setOwnerPool(String ownerPool) {
		_ownerPool = ownerPool;
	}

	public float getDistance() {
		return _distance;
	}

	public void setDistance(float distance) {
		_distance = distance;
	}

	public IReaderExt getReader() {
		return (IReaderExt) _reader_dy;
	}

	public void setReader(IReaderExt reader) {
		_reader_dy = (ImpinjReader) reader;
	}

	// public IStatusLight getStatusLight() {
	// return _statusLight;
	// }
	//
	// public void setStatusLight(IStatusLight statusLight) {
	// _statusLight = statusLight;
	// }
	//
	// public ICautionLight getCautionLight() {
	// return _cautionLight;
	// }
	//
	// public void setCautionLight(ICautionLight cautionLight) {
	// _cautionLight = cautionLight;
	// }

	public void setStatus(int status) {
		_status = status;
	}

	public int getStatus() {
		return _status;
	}

	@Override
	public Result rename(String name) {
		return Result.FAILURE;
	}

	@Override
	public String toString() {
		return "[name=" + _name + "," + "uuid=" + _uuid + "]";
	}

	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return "3.0.0-alpha";
	}

}
