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
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.NotificationCreate;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.NotificationDelete;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.NotificationModify;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseNotificationInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryNotificationInfo;

public class NotificationManagerTest {
	
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
	public void test() throws MessageBusException {
		// Setup for creation
		String uuid = UUID.randomUUID().toString();
		String content = "Notification Content";
		long time = (new Date()).getTime();
		long expire = time + 3600000;
		NotificationInfo notificationInfo = new NotificationInfo(uuid, content, time, expire);
		
		// Create
		AbstractMessage command = new NotificationCreate(notificationInfo);
		Confirmation confirmation = (Confirmation) _publisher.queryOne(command);
		assertNotNull(confirmation);
		assertEquals(Confirmation.RESULT_SUCCESS, confirmation.getResult());
		
		// Validate creation
		QueryNotificationInfo query = new QueryNotificationInfo(uuid);
		ResponseNotificationInfo response = (ResponseNotificationInfo) _publisher.queryOne(query);
		assertNotNull(response);
		assertEquals(content, response.getNotificationInfo().getContent());
		assertEquals(new Long(time), Long.valueOf(response.getNotificationInfo().getTime()));
		assertEquals(new Long(expire), Long.valueOf(response.getNotificationInfo().getExpire()));
		
		// Setup for modification
		content = "New Notification Content";
		time = (new Date()).getTime();
		expire = time + 3600000;
		notificationInfo = new NotificationInfo(uuid, content, time, expire);
		
		// Modify
		command = new NotificationModify(notificationInfo);
		confirmation = (Confirmation) _publisher.queryOne(command);
		assertNotNull(confirmation);
		assertEquals(Confirmation.RESULT_SUCCESS, confirmation.getResult());
		
		// Validate Modification
		response = (ResponseNotificationInfo) _publisher.queryOne(query);
		assertNotNull(response);
		assertEquals(content, response.getNotificationInfo().getContent());
		assertEquals(Long.valueOf(time), Long.valueOf(response.getNotificationInfo().getTime()));
		assertEquals(Long.valueOf(expire), response.getNotificationInfo().getExpire());
		
		// Delete
		command = new NotificationDelete(uuid);
		confirmation = (Confirmation) _publisher.queryOne(command);
		assertNotNull(confirmation);
		assertEquals(Confirmation.RESULT_SUCCESS, confirmation.getResult());
		
		// Validate Deletion
		response = (ResponseNotificationInfo) _publisher.queryOne(query);
		assertNotNull(response);
		assertNotNull(response.getNotificationInfo());
		assertEquals(true, response.getNotificationInfo().getDeleted());
	}

}
