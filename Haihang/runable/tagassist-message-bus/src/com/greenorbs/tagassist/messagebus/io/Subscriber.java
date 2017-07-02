/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.io;

import java.util.HashSet;

import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.MessageBusConfiguration;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.MessageBase;

public class Subscriber extends BasicSubscriber {
	
	private BasicPublisher _publisher;
	private Identifiable _sourceDevice;
	private boolean _muted;
	private HashSet<Integer> _whiteComponents;
	private HashSet<String> _whiteTopics;
	
	public Subscriber() {
		this(MessageBusConfiguration.brokerUri());
	}
	
	public Subscriber(String messageBusUri) {
		this(messageBusUri, null);
		_muted = false;
	}
	
	public Subscriber(Identifiable sourceDevice) {
		this(MessageBusConfiguration.brokerUri(), sourceDevice);
	}

	public Subscriber(String messageBusUri, Identifiable sourceDevice) {
		super(messageBusUri);
		_publisher = null;
		_sourceDevice = sourceDevice;
		_muted = true;
		_whiteComponents = new HashSet<Integer>();
		_whiteTopics = new HashSet<String>();
	}
	
	@Override
	protected void initialize() throws MessageBusException {
		_publisher = new BasicPublisher();
		_whiteComponents.add(Identifiable.COMPONENT_DATA_CENTER);
//		_whiteTopics.add(MessageHelper.getTopicName(HardwareInfoReport.class));
//		_whiteTopics.add(MessageHelper.getTopicName(TrackTunnelInfoReport.class));
//		_whiteTopics.add(MessageHelper.getTopicName(WristbandInfoReport.class));
		super.initialize();
	}
	
	@Override
	public void start() throws MessageBusException {
		if (!_started) {
			if (!_initialized) {
				this.initialize();
			}
			_publisher.start();
		}
		super.start();
	}
	
	@Override
	public void stop() throws MessageBusException {
		if (_started) {
			_publisher.stop();
		}
		super.stop();
	}
	
	@Override
	public void close() throws MessageBusException {
		if (_started) {
			_publisher.close();
		}
		super.close();
	}
	
	protected void prepareMessage(AbstractMessage message) {
		if (message instanceof MessageBase) {
			MessageBase m = (MessageBase) message;
			if (_sourceDevice != null) {
				if (m.getComponent() <= 0) {
					m.setComponent(_sourceDevice.getComponent());
				}
				if (m.getSource() == null) {
					m.setSource(_sourceDevice.getUUID());
				}
				if (m.getName() == null) {
					m.setName(_sourceDevice.getName());
				}
			}
		}
	}
	
	@Override
	public void reply(AbstractMessage to, AbstractMessage reply) throws MessageBusException {
		if (!shouldMute(MessageHelper.getTopicName(reply))) {
			this.prepareMessage(reply);
			super.reply(to, reply);
		}
	}
	
	public void confirm(AbstractMessage to, int result, String description) throws MessageBusException {
		if (to instanceof MessageBase && ((MessageBase) to).getNeedConfirm()) {
			GeneralMessages.Confirmation confirmation = new GeneralMessages.Confirmation(
					((MessageBase) to).getMessageId(), result, description);
			this.reply(to, confirmation);
		}
	}
	
	public void confirm(AbstractMessage to, int result) throws MessageBusException {
		this.confirm(to, result, null);
	}
	
	protected boolean shouldMute(String topicName) throws MessageBusException {
		if (!_muted || _sourceDevice == null 
				|| _whiteComponents.contains(_sourceDevice.getComponent())
				|| _whiteTopics.contains(topicName)) {
			return false;
		}
		
		QueryDeviceInfo query = new QueryDeviceInfo(_sourceDevice.getUUID());
		ResponseDeviceInfo reply = (ResponseDeviceInfo) _publisher.queryOne(query);
		if (reply != null && reply.getDeviceInfo() != null) {
			_muted = !reply.getDeviceInfo().getRegistered();
		}
		
		return _muted;
	}
	
	public Identifiable getSourceDevice() {
		return _sourceDevice;
	}

	public void setSourceDevice(Identifiable sourceDevice) {
		_sourceDevice = sourceDevice;
	}
	
}
