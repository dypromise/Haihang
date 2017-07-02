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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import com.greenorbs.tagassist.messagebus.MessageBusException;

public abstract class MapMessageHelper {
	
	protected static final String KEY_TOPIC_NAME = "TopicName";
	protected static final String KEY_MESSAGE = "Message";
	
	protected static Message createMessage(AbstractMessage message, 
			Session session) throws MessageBusException {
		
		try {
			String topicName = MessageHelper.getTopicName(message);
			String json = message.toJSON();
			
			MapMessage mqMessage = session.createMapMessage();
			mqMessage.setString(KEY_TOPIC_NAME, topicName);
			mqMessage.setString(KEY_MESSAGE, json);
			
			return mqMessage;
		} catch (JMSException e) {
			throw new MessageBusException(e);
		}
	}
	
	protected static Message createMessage(AbstractMessage message, 
			Session session, Destination replyTo) throws MessageBusException {
		
		try {
			Message mqMessage = createMessage(message, session);
			mqMessage.setJMSReplyTo(replyTo);
			
			return mqMessage;
		} catch (JMSException e) {
			throw new MessageBusException(e);
		}
	}
	
	protected static String extractTopicName(Message mqMessage) 
			throws MessageBusException {
		
		try {
			String topicName = ((MapMessage) mqMessage).getString(KEY_TOPIC_NAME);
			
			return topicName;
		} catch (JMSException e) {
			throw new MessageBusException(e);
		}
		
	}
	
	protected static AbstractMessage extractMessage(Message mqMessage) 
			throws MessageBusException {
		
		try {
			String className = ((MapMessage) mqMessage).getString(KEY_TOPIC_NAME);
			String json = ((MapMessage) mqMessage).getString(KEY_MESSAGE);
			AbstractMessage message = AbstractMessage.fromJSON(className, json);
			message.setDestination(mqMessage.getJMSDestination());
			message.setReplyTo(mqMessage.getJMSReplyTo());
			
			return message;
		} catch (JMSException e) {
			throw new MessageBusException(e);
		}
	}
	
}