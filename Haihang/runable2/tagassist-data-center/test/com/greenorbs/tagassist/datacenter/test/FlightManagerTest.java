/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
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
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFlightInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFlightList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFlightInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFlightList;

public class FlightManagerTest {
	
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
	public void testQueryFlightInfo() throws MessageBusException {
		QueryFlightInfo query = new QueryFlightInfo();
		query.setFlightId("ZLY402/14JAN");
		
		ArrayList<AbstractMessage> replies = _publisher.query(query);
		assertEquals(1, replies.size());
		assertEquals(ResponseFlightInfo.class, replies.get(0).getClass());
		
		ResponseFlightInfo reply = (ResponseFlightInfo) replies.get(0);
		assertEquals("ZLY402/14JAN", reply.getFlightInfo().getFlightId());
	}
	
	@Test
	public void testQueryFlightList() throws MessageBusException {
		QueryFlightList query = new QueryFlightList();
		
		ArrayList<AbstractMessage> replies = _publisher.query(query);
		assertEquals(1, replies.size());
		assertEquals(ResponseFlightList.class, replies.get(0).getClass());
		
		ResponseFlightList reply = (ResponseFlightList) replies.get(0);
		assertEquals(1, reply.getFlightInfoList().length);
		assertEquals("ZLY402/14JAN", reply.getFlightInfoList()[0].getFlightId());
	}
	
}
