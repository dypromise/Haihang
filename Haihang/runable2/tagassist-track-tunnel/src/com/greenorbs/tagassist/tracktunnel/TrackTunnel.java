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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.ICautionLight;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IReader;
import com.greenorbs.tagassist.device.IStatusLight;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.epc.EPC96;
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

public class TrackTunnel extends AbstractHardware implements IdentifyListener {

	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");

		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(
				"bean.xml");

		final TrackTunnel track = (TrackTunnel) ctx.getBean("trackTunnel");

		if (args.length > 0)
			_logger.info(args[0]);

		try {
			track.startup();

			// restart 2 hours later
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						track.reset();
					} catch (HardwareException e) {
						_logger.warn("Tracktunnel restart every 2 hours failed");
					}
				}

			}, 2 * 60 * 60 * 1000);

		} catch (HardwareException e) {
			_logger.error("Failed to start up the tracktunnel: " + track);
			try {
				track.shutdown();
			} catch (HardwareException e1) {
				e1.printStackTrace();
			}
		}

		ctx.close();
	}

	private static Logger _logger = Logger.getLogger(TrackTunnel.class);

	private FlightCache _flightCache;
	private BaggageCache _baggageCache;

	private static SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	private static SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	private static String[] MONTH = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
			"JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

	private String _uuid;
	private String _name;

	private String _ownerPool;
	private float _distance;
	// private Conveyor _conveyor;

	private TrackTunnelInfo _trackTunnelInfo;

	private IReader _reader;
	private IStatusLight _statusLight;
	private ICautionLight _cautionLight;
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

	public TrackTunnel() {
		super();

		_trackTunnelInfo = new TrackTunnelInfo();

		_flightCache = new FlightCache(_publisher);
		_baggageCache = new BaggageCache(_publisher);

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

				reply = new Confirmation(m.getMessageId(),
						Confirmation.RESULT_SUCCESS);
			}

		} else if (message instanceof BaggageArrival) {

			BaggageArrival m = (BaggageArrival) message;
			final BaggageInfo baggageInfo = m.getBaggageInfo();

			_baggageCache.put(baggageInfo);

			if (_simuMode) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						TrackTunnel.this.publishBaggageTracked(
								baggageInfo.getEPC(), -50.0f);
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
	public void startup() throws HardwareException {
		_logger.info("Starting up...");

		super.startup();

		_trackTunnelInfo.setUUID(this.getUUID());
		_trackTunnelInfo.setLocationParam1(this.getOwnerPool());
		_trackTunnelInfo.setLocationParam2(this.getDistance());

		// init cache immediately
		_flightCache.initialize();
		_baggageCache.initialize(_flightCache.flights());
		_logger.info("Cache initialized.");

		// start up reader
		if (_reader != null) {
			_reader.addIdentifyListener(this);
			_reader.startup();
			_logger.info("Reader started up.");
		} else {
			_logger.warn("Reader not found.");
		}

		// start up status light
		if (_statusLight != null) {
			_statusLight.startup();
			_statusLight.turnOff(IStatusLight.RED);
			_statusLight.turnOff(IStatusLight.YELLOW);
			_logger.info("Status light started up.");
		} else {
			_logger.warn("Status light not found.");
		}

		// start up caution light
		if (_cautionLight != null) {
			_cautionLight.startup();
			_cautionLight.turnOn(ICautionLight.GREEN);
			_logger.info("Caution light started up.");
		} else {
			_logger.warn("Caution light not found.");
		}

		this._status = IHardware.STATUS_ON;
		this.publishHardwareInfoReport();

		// this._conveyor.startSimulation(this._publisher, this._distance);
	}

	@Override
	public Result shutdown() throws HardwareException {
		_logger.info("Shutting down...");

		// shut down the reader
		if (_reader != null) {
			_reader.shutdown();
			_logger.info("Reader shut down.");
		}

		// shut down the status light
		if (_statusLight != null) {
			_statusLight.shutdown();
			_statusLight.turnOff(IStatusLight.YELLOW);
			_logger.info("Status light shut down.");
		}

		// shut down the caution light
		if (_cautionLight != null) {
			_cautionLight.shutdown();
			_cautionLight.turnOff(ICautionLight.BLACK);
			_logger.info("Caution light shut down.");
		}

		this._status = IHardware.STATUS_OFF;
		this.publishHardwareInfoReport();

		return super.shutdown();
	}

	@Override
	public Result reset() throws HardwareException {

		final String javaBin = System.getProperty("java.home") + File.separator
				+ "bin" + File.separator + "java";
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
		EPC96 epc = null;
		try {
			epc = new EPC96(observation.getEPC());
		} catch (Exception e) {
			_logger.error("Invalid epc encountered: " + observation.getEPC());
			return;
		}

		if (epc != null) {
			if (this._publishBaggageArrival) {

				this.publishBaggageArrival(epc);
			}

			if (this._publishBaggageTracked) {
//				_cachedThreadPool.execute(new Runnable() {
//					@Override
//					public void run() {
//						blinkStatusLight(IStatusLight.YELLOW,
//								OBSERVATION_BLINK_INTERVAL);
//					}
//				});

				this.publishBaggageTracked(epc.toString(),
						observation.getRssi());
			}
		}
	}

	private void publishBaggageArrival(EPC96 epc) {
		String baggageNumber = epc.getBaggageNumber();

		if (baggageNumber == null || _baggageCache.contains(baggageNumber)) {
			return;
		} else {
			_baggageCache.update(baggageNumber);
			if (_baggageCache.contains(baggageNumber)) {
				return;
			}
		}

		String flightCode = epc.getFlightCode();

		if (flightCode == null) {
			return;
		}

		long time = System.currentTimeMillis();
		String day = dayFormat.format(time);
		String month = MONTH[Integer.parseInt(monthFormat.format(time)) - 1];
		String year = yearFormat.format(time);

		String flightId = String.format("%s/%s%s%s", flightCode, day, month,
				year);

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

	private synchronized void blinkStatusLight(int light, long interval) {
		try {
			_statusLight.turnOn(light);
			System.out.println("ON:"+light+" "+interval);
			Thread.sleep(interval);
			_statusLight.turnOff(light);
			System.out.println("OFF:"+light+" "+interval);
		} catch (Exception ex) {
			_logger.error("Flash status light yellow failed");
		}
	}

	private void publishBaggageTracked(String epc, float rssi) {
		BaggageTracked tracked = new BaggageTracked();

		tracked.setEPC(epc);
		tracked.setPoolId(this.getOwnerPool());
		tracked.setDistance(this.getDistance());
		tracked.setRssi(rssi);

		// publish message
		this.publish(tracked);
		try {
			_cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					blinkStatusLight(IStatusLight.YELLOW,
							PUBLISH_MSG_BLINK_INTERVAL);
				}
			});
		} catch (Exception ex) {
			_logger.error("Flash status light green failed");
			_logger.error("Flash status light green failed");
		}

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

	// public Conveyor getConveyor() {
	// return _conveyor;
	// }
	//
	// public void setConveyor(Conveyor conveyor) {
	// _conveyor = conveyor;
	// }

	public IReader getReader() {
		return _reader;
	}

	public void setReader(IReader reader) {
		_reader = reader;
	}

	public IStatusLight getStatusLight() {
		return _statusLight;
	}

	public void setStatusLight(IStatusLight statusLight) {
		_statusLight = statusLight;
	}

	public ICautionLight getCautionLight() {
		return _cautionLight;
	}

	public void setCautionLight(ICautionLight cautionLight) {
		_cautionLight = cautionLight;
	}

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
