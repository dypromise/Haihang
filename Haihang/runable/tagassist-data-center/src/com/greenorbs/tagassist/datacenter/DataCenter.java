/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.datacenter;

import java.util.HashSet;

import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.datacenter.manager.BaggageManager;
import com.greenorbs.tagassist.datacenter.manager.CarriageManager;
import com.greenorbs.tagassist.datacenter.manager.CheckTunnelManager;
import com.greenorbs.tagassist.datacenter.manager.DeviceManager;
import com.greenorbs.tagassist.datacenter.manager.FacilityManager;
import com.greenorbs.tagassist.datacenter.manager.FlightManager;
import com.greenorbs.tagassist.datacenter.manager.MobileReaderManager;
import com.greenorbs.tagassist.datacenter.manager.NotificationManager;
import com.greenorbs.tagassist.datacenter.manager.TrackTunnelManager;
import com.greenorbs.tagassist.datacenter.manager.UserManager;
import com.greenorbs.tagassist.datacenter.manager.WristbandManager;
import com.greenorbs.tagassist.datacenter.replier.BaggageReplier;
import com.greenorbs.tagassist.datacenter.replier.CarriageReplier;
import com.greenorbs.tagassist.datacenter.replier.CheckTunnelReplier;
import com.greenorbs.tagassist.datacenter.replier.DeviceReplier;
import com.greenorbs.tagassist.datacenter.replier.FacilityReplier;
import com.greenorbs.tagassist.datacenter.replier.FlightReplier;
import com.greenorbs.tagassist.datacenter.replier.MobileReaderReplier;
import com.greenorbs.tagassist.datacenter.replier.NotificationReplier;
import com.greenorbs.tagassist.datacenter.replier.TrackTunnelReplier;
import com.greenorbs.tagassist.datacenter.replier.UserReplier;
import com.greenorbs.tagassist.datacenter.replier.WristbandReplier;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.util.AbstractHardware;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;
import com.impinj.octane.OctaneSdkException;

public class DataCenter extends AbstractHardware {
	
	/**
	 * @param args
	 * @throws OctaneSdkException 
	 */
	public static void main(String[] args) throws OctaneSdkException {
		DOMConfigurator.configure("log4j.xml");
		
		DataCenter.instance().start();
	}
	
	private static DataCenter _instance;
	private boolean _started;
	private HashSet<BusMessageHandler> _messageHandlers;

	private DataCenter() {
		super();
		
		_started = false;

	    _messageHandlers = new HashSet<BusMessageHandler>();
	    this.addBusMessageHandler(new BaggageManager());
	    this.addBusMessageHandler(new BaggageReplier());
		this.addBusMessageHandler(new CheckTunnelManager());
		this.addBusMessageHandler(new CheckTunnelReplier());
		this.addBusMessageHandler(new DeviceManager());
		this.addBusMessageHandler(new DeviceReplier());
		this.addBusMessageHandler(new FacilityManager());
		this.addBusMessageHandler(new FacilityReplier());
		this.addBusMessageHandler(new CarriageManager());
		this.addBusMessageHandler(new CarriageReplier());
		this.addBusMessageHandler(new FlightManager());
		this.addBusMessageHandler(new FlightReplier());
		this.addBusMessageHandler(new MobileReaderManager());
		this.addBusMessageHandler(new MobileReaderReplier());
		this.addBusMessageHandler(new NotificationManager());
		this.addBusMessageHandler(new NotificationReplier());
		this.addBusMessageHandler(new TrackTunnelManager());
		this.addBusMessageHandler(new TrackTunnelReplier());
		this.addBusMessageHandler(new UserManager());
		this.addBusMessageHandler(new UserReplier());
		this.addBusMessageHandler(new WristbandManager());
		this.addBusMessageHandler(new WristbandReplier());
	}
	
	public static DataCenter instance() {
		// Double-checked locking
		if (null == _instance) {
			synchronized (DataCenter.class) {
				if (null == _instance) {
					_instance = new DataCenter();
				}
			}
		}
		return _instance;
	}
	
	private void addBusMessageHandler(BusMessageHandler handler) {
		handler.setPublisher(_publisher);
		handler.setSubscriber(_subscriber);
		_messageHandlers.add(handler);
	}
	
	public void start() throws OctaneSdkException {
		if (!_started) {
			_internalStart();
		}
	}
	
	private void _internalStart() throws OctaneSdkException{
		try {
			_logger.info("Data center is starting up.");
			
			super.startup();
			
			for (BusMessageHandler handler : _messageHandlers) {
				handler.initialize();
			}
		
			_started = true;
			
			_logger.info("Data center started.");
		} catch (Exception e) {
			_logger.error(e.getMessage());
		}
	}
	
	public void close() {
		if (_started) {
			try {
				_publisher.stop();
				_publisher.close();
				
				_subscriber.stop();
				_subscriber.close();
				
				HibernateUtils.closeSessionFactory();
				
				_started = false;
			} catch (Exception e) {
				_logger.error(e.getMessage());
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (_started) {
			this.close();
		}
		super.finalize();
	}

	@Override
	public void startup() throws HardwareException, OctaneSdkException {
		this.start();
	}

	@Override
	public int getStatus() {
		return IHardware.STATUS_ON;
	}
	
	@Override
	public int getComponent() {
		return Identifiable.COMPONENT_DATA_CENTER;
	}

	@Override
	public String getUUID() {
		return DataCenterConfiguration.uuid();
	}

	@Override
	public String getName() {
		return DataCenterConfiguration.name();
	}

	@Override
	public Result rename(String name) {
		return DataCenterConfiguration.name(name);
	}
	
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return "3.0.0-alpha";
	}
	
}
