/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 12, 2012
 */

package com.greenorbs.tagassist.messagebus.io;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.greenorbs.tagassist.messagebus.MessageBusConfiguration;
import com.greenorbs.tagassist.messagebus.MessageBusException;

public class BasicSubscriber implements MessageListener {

	protected static Logger _logger = Logger.getLogger(BasicSubscriber.class);
	
	protected boolean _initialized;
	protected boolean _started;
	protected String _messageBusUri;
	protected Connection _connection;
	protected Session _session;
	protected Hashtable<String, MessageConsumer> _consumers;
	protected Hashtable<String, IMessageHandler> _handlers;
	
	public BasicSubscriber() {
		this(MessageBusConfiguration.brokerUri());
	}
	
	public BasicSubscriber(String messageBusUri) {
		_initialized = false;
		_started = false;
		_messageBusUri = messageBusUri;
		_connection = null;
		_session = null;
		_consumers = new Hashtable<String, MessageConsumer>();
		_handlers = new Hashtable<String, IMessageHandler>();
	}
	
	protected void initialize() throws MessageBusException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(_messageBusUri);
		try {
	        _connection = factory.createConnection();
	        _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        for (String topicName : _handlers.keySet()) {
	        	Topic topic = _session.createTopic(topicName);
	        	MessageConsumer consumer = _session.createConsumer(topic);
	        	consumer.setMessageListener(this);
	        	
	        	_consumers.put(topicName, consumer);
	        }
		} catch (JMSException e) {
			throw new MessageBusException(e);
		}
        _initialized = true;
	}
	
	public void start() throws MessageBusException {
		if (!_started) {
			if (!_initialized) {
				this.initialize();
			}
			try {
				_connection.start();
			} catch (JMSException e) {
				throw new MessageBusException(e);
			}
	        _started = true;
		}
	}
	
	public void stop() throws MessageBusException {
		if (_started) {
			try {
				_connection.stop();
			} catch (JMSException e) {
				throw new MessageBusException(e);
			}
			_started = false;
		}
	}
	
	public void close() throws MessageBusException {
		if (_started) {
			try {
				for (MessageConsumer c : _consumers.values()) {
					c.close();
				}
				_session.close();
				_connection.close();
			} catch (JMSException e) {
				throw new MessageBusException(e);
			}
			_started = false;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (_started) {
			this.close();
		}
		super.finalize();
	}
	
	public void subscribe(Class<?> topicClass, IMessageHandler handler) throws MessageBusException {
		this.subscribe(MessageHelper.getTopicName(topicClass), handler);
	}
	
	public void subscribe(String topicName, IMessageHandler handler) throws MessageBusException {
		if (_started) {
			if (!_handlers.containsKey(topicName)) {
				try {
					Topic topic = _session.createTopic(topicName);
					MessageConsumer consumer = _session.createConsumer(topic);
					consumer.setMessageListener(this);
					
					_consumers.put(topicName, consumer);
				} catch (JMSException e) {
					throw new MessageBusException(e);
				}
				_handlers.put(topicName, handler);
			}
		} else {
			_handlers.put(topicName, handler);
		}
	}
	
	public void reply(AbstractMessage to, AbstractMessage reply) throws MessageBusException {
		if (_started) {
			Destination replyTo = to.getReplyTo();
			if (replyTo != null) {
				try {
					MessageProducer tempProducer = _session.createProducer(replyTo);
					
					Message mqMessage = MessageHelper.createMessage(reply, _session);
					
					tempProducer.send(mqMessage);
					tempProducer.close();
				} catch (JMSException e) {
					throw new MessageBusException(e);
				}
			} else {
				_logger.error("replyTo is not set properly.");
			}
		}
	}
	
	protected void dispatchMessage(Message message) {
		try {
			String topicName = MessageHelper.extractTopicName(message);
			if (_handlers.containsKey(topicName)) {
				IMessageHandler h = _handlers.get(topicName);
				AbstractMessage m = MessageHelper.extractMessage(message);
				h.onMessage(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error(e);
		}
	}

	@Override
	public void onMessage(Message message) {
		this.dispatchMessage(message);
	}
	
}
