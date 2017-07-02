/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 1, 2012
 */

package com.greenorbs.tagassist.flightproxy;

import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.flightproxy.dcsadapter.DCSAdapter;
import com.greenorbs.tagassist.messagebus.util.AbstractHardware;

public class FlightProxy extends AbstractHardware {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");
		
		FlightProxy.instance().start();
	}
	
	private static FlightProxy _instance;
	private boolean _started;
	private FlightProxyWorker _worker;
	private DCSAdapter _dcsAdapter;
	
	private FlightProxy() {
		super();

		_started = false;
		
		_worker = new FlightProxyWorker(_publisher, _subscriber);
		
		_dcsAdapter = DCSAdapter.instance();
		_dcsAdapter.registerMessageConsumer(_worker);
	}
	
	public static FlightProxy instance() {
		// Double-checked locking
		if (null == _instance) {
			synchronized (FlightProxy.class) {
				if (null == _instance) {
					_instance = new FlightProxy();
				}
			}
		}
		return _instance;
	}
	
	public void start() {
		if (!_started) {
			_internalStart();
		}
	}
	
	private void _internalStart() {
		try {
			_logger.info("Flight proxy is starting up.");
			
			super.startup();
			
			// The initialization of worker may be time-consuming, but we do 
			// need to initialize it before the adapters (threads) start.
			_worker.initialize();
			
			_dcsAdapter.start();
			
			_started = true;
			
			_logger.info("Flight proxy started.");
		} catch (Exception e) {
			_logger.error(e);
		}
	}
	
	public void close() {
		if (_started) {
			try {
				_publisher.stop();
				_publisher.close();
				
				_subscriber.stop();
				_subscriber.close();
				
				_dcsAdapter.exit = true;
				_dcsAdapter.join();
				
				_started = false;
			} catch (Exception e) {
				_logger.error(e);
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
	public void startup() throws HardwareException {
		this.start();
	}

	@Override
	public int getStatus() {
		return IHardware.STATUS_ON;
	}
	
	@Override
	public int getComponent() {
		return Identifiable.COMPONENT_FLIGHT_PROXY;
	}

	@Override
	public String getUUID() {
		return FlightProxyConfiguration.uuid();
	}

	@Override
	public String getName() {
		return FlightProxyConfiguration.name();
	}

	@Override
	public Result rename(String name) {
		return FlightProxyConfiguration.name(name);
	}
	
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return "3.0.0-alpha";
	}
	
}
