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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.IMessageHandler;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.io.Subscriber;

public class PublisherTest {
	
	private class MessageReplier implements IMessageHandler {
		
		Subscriber _subscriber;
		AbstractMessage _reply;
		
		public MessageReplier(Subscriber subscriber, AbstractMessage reply) {
			_subscriber = subscriber;
			_reply = reply;
		}

		@Override
		public void onMessage(AbstractMessage message) {
			try {
				_subscriber.reply(message, _reply);
			} catch (MessageBusException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Publisher _publisher;
	private ArrayList<Subscriber> _subscribers;
	
	private CountDownLatch _lock = new CountDownLatch(1);
	private TestMessage _receivedMessage;

	@Before
	public void setUp() throws Exception {
		_publisher = new Publisher();
		_publisher.start();
		
		_subscribers = new ArrayList<Subscriber>();
	}

	@After
	public void tearDown() throws Exception {
		_publisher.close();
		_publisher = null;
		
		for (Subscriber subscriber : _subscribers) {
			subscriber.close();
			subscriber = null;
		}
		_subscribers.clear();
	}

	@Test
	public void testPublish() throws MessageBusException, InterruptedException {
		_subscribers.add(new Subscriber());
		_subscribers.get(0).start();
		
		_subscribers.get(0).subscribe(TestMessage.class, new IMessageHandler() {
			
			@Override
			public void onMessage(AbstractMessage message) {
				try {
					_receivedMessage = ((TestMessage) message).clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		});
		
		TestMessage message = new TestMessage();
		message.setIntField(100);
		message.setStringField("Test");
		message.setBooleanField(false);
		
		_publisher.publish(message);
		
		synchronized (_lock)
	    {
	        _lock.await(_publisher.getDefaultQueryTimeout(), TimeUnit.MILLISECONDS);
	    }
		
		assertEquals(100, _receivedMessage.getIntField());
		assertEquals("Test", _receivedMessage.getStringField());
		assertEquals(false, _receivedMessage.getBooleanField());
	}

	@Test
	public void testQuery() throws MessageBusException {
		int rounds = 10;
		
		for (int i = 0; i < rounds; i++) {
			_subscribers.add(new Subscriber());
			_subscribers.get(i).start();
			
			TestMessage reply = new TestMessage();
			reply.setIntField(i);
			
			_subscribers.get(i).subscribe(TestMessage.class, 
					new MessageReplier(_subscribers.get(i), reply));
		}
		
		ArrayList<AbstractMessage> replies = _publisher.query(new TestMessage());
		
		assertEquals(rounds, replies.size());
		
		if (replies.size() == rounds) {
			int n1 = 0;
			int n2 = 0;
			for (int i = 0; i < rounds; i++) {
				n1 += i;
				n2 += ((TestMessage) replies.get(i)).getIntField();
			}
			assertEquals(n1, n2);
		}
	}

}
