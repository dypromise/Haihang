/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 10, 2012
 */

package com.greenorbs.tagassist.messagebus.util;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.IMessageHandler;
import com.greenorbs.tagassist.messagebus.io.MessageHelper;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.io.Subscriber;
import com.greenorbs.tagassist.messagebus.message.MessageBase;

public abstract class BusMessageHandler implements IMessageHandler {
	
	protected Logger _logger = Logger.getLogger(this.getClass());
	
	protected Publisher _publisher;
	protected Subscriber _subscriber;
	
	public BusMessageHandler() {}
	
	public BusMessageHandler(Publisher publisher, Subscriber subscriber) {
		_publisher = publisher;
		_subscriber = subscriber;
	}
	
	public void initialize() {
		this.initSubscriber();
	}

	protected abstract void initSubscriber();
	
	protected abstract MessageBase handleMessage(MessageBase message);
	
	public void subscribe(Class<?> topicClass) {
		try {
			_subscriber.subscribe(topicClass, this);
			_logger.info("Topic subscribed: " + MessageHelper.getTopicName(topicClass));
		} catch (MessageBusException e) {
			e.printStackTrace();
			_logger.error("Failed to subscribe topic: " + MessageHelper.getTopicName(topicClass));
		}
	}
	
	@Override
	public void onMessage(AbstractMessage message) {
		if (message instanceof MessageBase) {
			_logger.info("Handling message: " + message);
			MessageBase reply = this.handleMessage((MessageBase) message);
			if (reply != null) {
				try {
					_subscriber.reply(message, reply);
					_logger.info("Replied with: " + reply);
				} catch (Exception e) {
					e.printStackTrace();
					_logger.error("Reply not sent: " + e.getMessage());
				}
			}
		}
	}
	
	public void publish(AbstractMessage message) {
		try {
			_publisher.publish(message);
			_logger.info("Message published: " + message);
		} catch (MessageBusException e) {
			e.printStackTrace();
			_logger.error("Message not published: " + e.getMessage());
		}
	}
	
	public void setPublisher(Publisher publisher) {
		_publisher = publisher;
	}

	public void setSubscriber(Subscriber subscriber) {
		_subscriber = subscriber;
	}

}
