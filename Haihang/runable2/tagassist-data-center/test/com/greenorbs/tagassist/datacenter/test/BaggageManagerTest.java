/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 9, 2012
 */

package com.greenorbs.tagassist.datacenter.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageCheckTunnel;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageCheckTunnel;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageList;

public class BaggageManagerTest {
	
	private static Publisher _publisher;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_publisher = new Publisher();
		_publisher.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		_publisher.close();
		_publisher = null;
	}

	@Test
	public void testQueryBaggageInfo() throws MessageBusException {
		QueryBaggageInfo query = new QueryBaggageInfo();
		query.setBaggageNumber("NUM1234567890");
		
		ArrayList<AbstractMessage> replies = _publisher.query(query);
		assertEquals(1, replies.size());
		assertEquals(ResponseBaggageInfo.class, replies.get(0).getClass());
		
		ResponseBaggageInfo reply = (ResponseBaggageInfo) replies.get(0);
		assertEquals("NUM1234567890", reply.getBaggageInfo().getNumber());
	}
	
	@Test
	public void testQueryBaggageList() throws MessageBusException {
		QueryBaggageList query = new QueryBaggageList();
		query.setFlightId("ZLY402/14JAN");
		
		ArrayList<AbstractMessage> replies = _publisher.query(query);
		assertEquals(1, replies.size());
		assertEquals(ResponseBaggageList.class, replies.get(0).getClass());
		
		ResponseBaggageList reply = (ResponseBaggageList) replies.get(0);
		assertEquals(1, reply.getBaggageInfoList().length);
		assertEquals("NUM1234567890", reply.getBaggageInfoList()[0].getNumber());
	}
	
	@Test
	public void testQueryBaggageCheckTunnel() throws MessageBusException {
		QueryBaggageCheckTunnel query = new QueryBaggageCheckTunnel();
		query.setBaggageNumber("NUM1234567890");
		
		ArrayList<AbstractMessage> replies = _publisher.query(query);
		assertEquals(1, replies.size());
		assertEquals(ResponseBaggageCheckTunnel.class, replies.get(0).getClass());
		
		ResponseBaggageCheckTunnel reply = (ResponseBaggageCheckTunnel) replies.get(0);
		assertEquals("5f9c0d40-506f-11e1-b86c-0800200c9a66", reply.getCheckTunnelId());
	}

}
