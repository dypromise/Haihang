/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, Xuan Ding 2012-2-27
 */

package com.greenorbs.tagassist.messagebus.util;

import java.util.Timer;
import java.util.TimerTask;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IResumableHardware;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.MessageBusConfiguration;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.io.Subscriber;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareCollect;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareControl;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareInfoGet;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareInfoSet;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Heartbeat;
import com.greenorbs.tagassist.messagebus.message.MessageBase;

public abstract class AbstractHardware extends BusMessageHandler implements IResumableHardware, Identifiable {

	private final Heartbeat _heartbeat;
	private Timer _heartbeatTimer;
	
	public AbstractHardware() {
		_publisher = new Publisher(this);
		_subscriber = new Subscriber(this);
		_heartbeat = new Heartbeat();
		_heartbeatTimer = null;
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(HardwareControl.class);
		this.subscribe(HardwareInfoSet.class);
		this.subscribe(HardwareInfoGet.class);
		this.subscribe(HardwareCollect.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof HardwareControl) {
			String targetUUID = ((HardwareControl) message).getTargetUUID();
			
			if (this.getUUID().equals(targetUUID)) {
				int command = ((HardwareControl) message).getCommand();
				Result result = this.handleCommand(command);
				
				reply = new Confirmation(message.getMessageId(), result.getCode(), result.getDescription());
				
				if (result.getCode() == Result.CODE_SUCCESS) {
					this.publishHardwareInfoReport();
				}
			}
			
		} else if (message instanceof HardwareInfoSet) {
			String targetUUID = ((HardwareInfoSet) message).getTargetUUID();

			if (targetUUID.equals(this.getUUID())) {
				String newName = ((HardwareInfoSet) message).getName();
				Result result = this.setHardwareInfo(newName);
				
				reply = new Confirmation(message.getMessageId(), result.getCode(), result.getDescription());
				
				if (result.getCode() == Result.CODE_SUCCESS) {
					this.publishHardwareInfoReport();
				}
			}
		} else if (message instanceof HardwareInfoGet) {
			String targetUUID = ((HardwareInfoSet) message).getTargetUUID();

			if (targetUUID.equals(this.getUUID())) {
				reply = this.generateHardwareInfoReport();
			}
		} else if (message instanceof HardwareCollect) {
			this.publishHardwareInfoReport();
		}
		
		return reply;
	}
	
	private Result handleCommand(int command) {
		Result result = new Result();
		
		try {
			switch (command) {
			case IHardware.COMMAND_RESET: {
				result = this.reset();
				break;
			}
			case IHardware.COMMAND_PAUSE: {
				result = this.pause();
				break;
			}
			case IHardware.COMMAND_RESUME: {
				result = this.resume();
				break;
			}
			case IHardware.COMMAND_SHUTDOWN: {
				result = this.shutdown();
				break;
			}
			default: {
				break;
			}
			}
		} catch (HardwareException e) {
			result.setCode(Result.CODE_ERROR);
			result.setDescription(e.getMessage());
			
			e.printStackTrace();
			_logger.error(e.getMessage());
		}
		
		return result;
	}

	private Result setHardwareInfo(String newName) {
		return this.rename(newName);
	}
	
	protected void publishHardwareInfoReport() {
		this.publish(this.generateHardwareInfoReport());
	}
	
	protected HardwareInfoReport generateHardwareInfoReport() {
		HardwareInfoReport report = new HardwareInfoReport();
		
		report.setName(this.getName());
		report.setStatus(this.getStatus());
		report.setSoftwareVersion(this.getSoftwareVersion());
		report.setHostname(this.getHostname());
		report.setMacAddress(this.getMacAddress());
		report.setIpAddress(this.getIpAddress());
		
		return report;
	}
	
	protected String getHostname() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String getMacAddress() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String getIpAddress() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void customizeHeartbeat(Heartbeat heartbeat) { /* EMPTY */ }

	@Override
	public void startup() throws HardwareException {
		this.initialize();
		
		try {
			_publisher.start();
			_subscriber.start();
		} catch (MessageBusException e) {
			e.printStackTrace();
			_logger.error(e.getMessage());
		}
		
		_heartbeatTimer = new Timer();
		_heartbeatTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					AbstractHardware.this.customizeHeartbeat(_heartbeat);
					_publisher.publish(_heartbeat);
				} catch (MessageBusException e) {
					e.printStackTrace();
					_logger.error(e.getMessage());
				}
			}
			
		}, 1000, MessageBusConfiguration.heartbeatInterval());
		
		this.publishHardwareInfoReport();
	}

	@Override
	public Result shutdown() throws HardwareException {
		if (_heartbeatTimer != null) {
			_heartbeatTimer.cancel();
		}
		
		try {
			_publisher.stop();
			_publisher.close();
			
			_subscriber.stop();
			_subscriber.close();
		} catch (MessageBusException e) {
			e.printStackTrace();
			_logger.error(e.getMessage());
		}
		
		return new Result(Result.CODE_SUCCESS);
	}

	@Override
	public Result reset() throws HardwareException {
		return new Result(Result.CODE_SUCCESS);
	}
	
	@Override
	public Result pause() throws HardwareException {
		return new Result(Result.CODE_SUCCESS);
	}
	
	@Override
	public Result resume() throws HardwareException {
		return new Result(Result.CODE_SUCCESS);
	}
	
}
