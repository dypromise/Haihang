/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 13, 2012
 */

package com.greenorbs.tagassist.datacenter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.WristbandInfo;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseWristbandInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryWristbandInfo;

public class WristbandManagerTest {
	
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
	public void testQueryWristbandInfo() throws MessageBusException {
		QueryWristbandInfo query = new QueryWristbandInfo();
		query.setWristbandId("ac7db380-5596-11e1-b86c-0800200c9a66");
		
		ArrayList<AbstractMessage> replies = _publisher.query(query);
		assertEquals(1, replies.size());
		assertEquals(ResponseWristbandInfo.class, replies.get(0).getClass());
		
		ResponseWristbandInfo reply = (ResponseWristbandInfo) replies.get(0);
		assertNotNull(reply.getWristbandInfo());
		
		WristbandInfo wristbandInfo = reply.getWristbandInfo();
		assertEquals("ac7db380-5596-11e1-b86c-0800200c9a66", wristbandInfo.getUUID());
		
		String[] boundFlightIdList = wristbandInfo.getBoundFlightIdList();
		assertEquals(1, boundFlightIdList.length);
		assertEquals("CP161/20NOV2012", boundFlightIdList[0]);
	}

}
