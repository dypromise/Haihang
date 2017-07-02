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

//import javax.jms.Destination;
//import javax.jms.MapMessage;
//import javax.jms.Message;
//import javax.jms.Session;
//
//import com.greenorbs.tagassist.messagebus.MessageBusException;

public class MessageHelper extends TextMessageHelper {
	
	public static String getTopicName(Class<?> c) {
		return c.getName();
	}
	
	public static String getTopicName(AbstractMessage message) {
		return message.getClass().getName();
	}
	
//	protected static Message createMessage(AbstractMessage message, 
//			Session session) throws MessageBusException {
//		
//		return MapMessageHelper.createMessage(message, session);
//	}
//	
//	protected static Message createMessage(AbstractMessage message, 
//			Session session, Destination replyTo) throws MessageBusException {
//		
//		return MapMessageHelper.createMessage(message, session, replyTo);
//	}
//	
//	protected static String extractTopicName(Message message) 
//			throws MessageBusException {
//		
//		return MapMessageHelper.extractTopicName((MapMessage) message);
//	}
//	
//	protected static AbstractMessage extractMessage(Message message) 
//			throws MessageBusException {
//		
//		return MapMessageHelper.extractMessage((MapMessage) message);
//	}
	
}