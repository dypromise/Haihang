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

import javax.jms.Destination;

import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.MessageBusConfiguration;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Heartbeat;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.MessageBase;

public class Publisher extends BasicPublisher {
	
	private Identifiable _sourceDevice;
	private boolean _muted;
	private HashSet<Integer> _whiteComponents;
	private HashSet<String> _whiteTopics;

	public Publisher() {
		this(MessageBusConfiguration.brokerUri());
	}

	public Publisher(String messageBusUri) {
		this(messageBusUri, null);
		_muted = false;
	}

	public Publisher(Identifiable sourceDevice) {
		this(MessageBusConfiguration.brokerUri(), sourceDevice);
	}
	
	public Publisher(String messageBusUri, Identifiable sourceDevice) {
		super(messageBusUri);
		_sourceDevice = sourceDevice;
		_muted = false;
		_whiteComponents = new HashSet<Integer>();
		_whiteTopics = new HashSet<String>();
	}
	
	@Override
	protected void initialize() throws MessageBusException {
		_whiteComponents.add(Identifiable.COMPONENT_FLIGHT_PROXY);
		_whiteComponents.add(Identifiable.COMPONENT_DATA_CENTER);
		_whiteComponents.add(Identifiable.COMPONENT_ADMINISTRATOR);
		_whiteTopics.add(MessageHelper.getTopicName(Heartbeat.class));
//		_whiteTopics.add(MessageHelper.getTopicName(HardwareInfoReport.class));
//		_whiteTopics.add(MessageHelper.getTopicName(TrackTunnelInfoReport.class));
//		_whiteTopics.add(MessageHelper.getTopicName(WristbandInfoReport.class));
		_whiteTopics.add(MessageHelper.getTopicName(QueryDeviceInfo.class));
		super.initialize();
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
	protected void publish(String topicName, AbstractMessage message, Destination replyTo) throws MessageBusException {
		if (!shouldMute(topicName)) {
			this.prepareMessage(message);
			super.publish(topicName, message, replyTo);
		}
	}
	
	protected boolean shouldMute(String topicName) throws MessageBusException {
		if (!_muted || _sourceDevice == null 
				|| _whiteComponents.contains(_sourceDevice.getComponent())
				|| _whiteTopics.contains(topicName)) {
			return false;
		}
		
		QueryDeviceInfo query = new QueryDeviceInfo(_sourceDevice.getUUID());
		ResponseDeviceInfo reply = (ResponseDeviceInfo) this.queryOne(query);
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
