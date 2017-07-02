/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.IMessageHandler;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.io.Subscriber;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages;

public class SubscriberTest {

	private Publisher _publisher;
	private Subscriber _subscriber;
	
	@Before
	public void setUp() throws Exception {
		_publisher = new Publisher();
		_publisher.start();
		
		_subscriber = new Subscriber();
		_subscriber.start();
	}

	@After
	public void tearDown() throws Exception {
		_publisher.close();
		_publisher = null;
		
		_subscriber.close();
		_subscriber = null;
	}
	
	@Test
	public void testSubscribe() throws MessageBusException {
		_subscriber.subscribe(TestMessage.class, new IMessageHandler() {
			
			@Override
			public void onMessage(AbstractMessage message) {
				System.out.println(message.toJSON());
			}
		});
		
		_subscriber.subscribe(FlightProxyMessages.FlightCheckingIn.class, new IMessageHandler() {
			
			@Override
			public void onMessage(AbstractMessage message) {
				System.out.println(message.toJSON());
			}
		});
		
		_publisher.publish(new TestMessage());
		_publisher.publish(new FlightProxyMessages.FlightCheckingIn());
	}

	@Test
	public void testReply() throws MessageBusException {
		_subscriber.subscribe(TestMessage.class, new IMessageHandler() {
			
			@Override
			public void onMessage(AbstractMessage message) {
				TestMessage reply = new TestMessage();
				reply.setIntField(100);
				reply.setStringField("Test");
				reply.setBooleanField(false);
				
				try {
					_subscriber.reply(message, reply);
				} catch (MessageBusException e) {
					e.printStackTrace();
				}
			}
		});
		
		ArrayList<AbstractMessage> replies = _publisher.query(new TestMessage());
		
		assertEquals(1, replies.size());
		
		if (replies.size() == 1) {
			assertEquals(100, ((TestMessage) replies.get(0)).getIntField());
			assertEquals("Test", ((TestMessage) replies.get(0)).getStringField());
			assertEquals(false, ((TestMessage) replies.get(0)).getBooleanField());
		}
	}

}
