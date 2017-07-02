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
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.greenorbs.tagassist.messagebus.MessageBusException;

public abstract class TextMessageHelper {
	
	protected static Message createMessage(AbstractMessage message, 
			Session session) throws MessageBusException {
		
		try {
			String topicName = MessageHelper.getTopicName(message);
			String json = message.toJSON();
			
			TextMessage mqMessage = session.createTextMessage();
			mqMessage.setText(String.format("%s,%s", topicName, json));
			
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
			String text = ((TextMessage) mqMessage).getText();
			int index = text.indexOf(",");
			if (index >= 0) {
				String topicName = text.substring(0, index);
				
				return topicName;
			} else {
				throw new MessageBusException("Invalid message encountered.");
			}
		} catch (JMSException e) {
			throw new MessageBusException(e);
		}
		
	}
	
	protected static AbstractMessage extractMessage(Message mqMessage) 
			throws MessageBusException {
		
		try {
			String text = ((TextMessage) mqMessage).getText();
			int index = text.indexOf(",");
			if (index >= 0) {
				String className = text.substring(0, index);
				String json = text.substring(index + 1);
				AbstractMessage message = AbstractMessage.fromJSON(className, json);
				message.setDestination(mqMessage.getJMSDestination());
				message.setReplyTo(mqMessage.getJMSReplyTo());
				
				return message;
			} else {
				throw new MessageBusException("Invalid message encountered.");
			}
		} catch (JMSException e) {
			throw new MessageBusException(e);
		}
	}
	
}