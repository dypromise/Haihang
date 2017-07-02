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

import java.util.ArrayList;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.greenorbs.tagassist.messagebus.MessageBusConfiguration;
import com.greenorbs.tagassist.messagebus.MessageBusException;

public class BasicPublisher {

	protected static Logger _logger = Logger.getLogger(BasicPublisher.class);

	protected boolean _initialized;
	protected boolean _started;
	protected String _messageBusUri;
	protected Connection _connection;
	protected Session _session;
	protected int _defaultQueryTimeout;
	protected Hashtable<String, MessageProducer> _producers;
	
	public boolean isStarted() {
		return _started;
	}

	public int getDefaultQueryTimeout() {
		return _defaultQueryTimeout;
	}

	public void setDefaultQueryTimeout(int defaultQueryTimeout) {
		_defaultQueryTimeout = defaultQueryTimeout;
	}

	public BasicPublisher() {
		this(MessageBusConfiguration.brokerUri());
	}

	public BasicPublisher(String messageBusUri) {
		_initialized = false;
		_started = false;
		_messageBusUri = messageBusUri;
		_connection = null;
		_session = null;
		_defaultQueryTimeout = MessageBusConfiguration.defaultQueryTimeout();
		_producers = new Hashtable<String, MessageProducer>();
	}
	
	protected void initialize() throws MessageBusException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(_messageBusUri);
		try {
	        _connection = factory.createConnection();
	        _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
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
				for (MessageProducer producer : _producers.values()) {
					producer.close();
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
	
	protected void publish(String topicName, AbstractMessage message, Destination replyTo) throws MessageBusException {
		if (_started) {
			try {
				if (!_producers.containsKey(topicName)) {
					Topic topic = _session.createTopic(topicName);
				 	MessageProducer producer = _session.createProducer(topic);
				 	producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
					_producers.put(topicName, producer);
				}
				
				Message mqMessage = MessageHelper.createMessage(message, _session, replyTo);
				
				_producers.get(topicName).send(mqMessage);
			} catch (JMSException e) {
				throw new MessageBusException(e);
			}
		}
	}
	
	protected void publish(AbstractMessage message, Destination replyTo) throws MessageBusException {
		String topicName = MessageHelper.getTopicName(message);
		this.publish(topicName, message, replyTo);
	}
	
	public void publish(String topicName, AbstractMessage message) throws MessageBusException {
		this.publish(topicName, message, null);
	}
	
	public void publish(AbstractMessage message) throws MessageBusException {
		String topicName = MessageHelper.getTopicName(message);
		this.publish(topicName, message);
	}
	
	public ArrayList<AbstractMessage> query(AbstractMessage message, long timeout) throws MessageBusException {
		ArrayList<AbstractMessage> replies = new ArrayList<AbstractMessage>();
		
		if (_started) {
			try {
				TemporaryQueue tempQueue = _session.createTemporaryQueue();
				MessageConsumer tempConsumer = _session.createConsumer(tempQueue);
				
				this.publish(message, tempQueue);
				
				while (true) {
					Message received = tempConsumer.receive(timeout);
					if (received != null) {
						replies.add(MessageHelper.extractMessage(received));
					} else {
						break;
					}
				}
				
				tempConsumer.close();
				tempQueue.delete();
			} catch (JMSException e) {
				throw new MessageBusException(e);
			}
		}
		
		return replies;
	}
	
	public ArrayList<AbstractMessage> query(AbstractMessage message) throws MessageBusException {
		return this.query(message, _defaultQueryTimeout);
	}
	
	public AbstractMessage queryOne(AbstractMessage message, long timeout) throws MessageBusException {
		AbstractMessage reply = null;
		
		if (_started) {
			try {
				TemporaryQueue tempQueue = _session.createTemporaryQueue();
				MessageConsumer tempConsumer = _session.createConsumer(tempQueue);
				
				this.publish(message, tempQueue);
				
				Message received = tempConsumer.receive(timeout);
				if (received != null) {
					reply = MessageHelper.extractMessage(received);
				}
				
				tempConsumer.close();
				tempQueue.delete();
			} catch (JMSException e) {
				throw new MessageBusException(e);
			}
		}
		
		return reply;
	}
	
	public AbstractMessage queryOne(AbstractMessage message) throws MessageBusException {
		return this.queryOne(message, _defaultQueryTimeout);
	}
	
}
