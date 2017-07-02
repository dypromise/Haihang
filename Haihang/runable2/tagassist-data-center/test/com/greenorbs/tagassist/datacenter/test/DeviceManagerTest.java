/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 16, 2012
 */

package com.greenorbs.tagassist.datacenter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.DeviceRegister;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.MessageBase;

public class DeviceManagerTest {
	
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
	public void testDeviceRegister() throws MessageBusException {
		String uuid = UUID.randomUUID().toString();
		int component = Identifiable.COMPONENT_CHECK_TUNNEL;
		String name = "TEST DEVICE";
		
		MessageBase message = new HardwareInfoReport();
		message.setSource(uuid);
		message.setComponent(component);
		message.setName(name);
		
		_publisher.publish(message);
		try {
			// Sleep for some seconds and let data center do its job
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		message = new DeviceRegister(uuid);
		AbstractMessage reply = _publisher.queryOne(message);
		assertNotNull(reply);
		assertEquals(Confirmation.class, reply.getClass());
		assertEquals(Confirmation.RESULT_SUCCESS, ((Confirmation) reply).getResult());
		
		message = new QueryDeviceInfo(uuid);
		reply = _publisher.queryOne(message);
		assertNotNull(reply);
		assertEquals(ResponseDeviceInfo.class, reply.getClass());
		
		DeviceInfo deviceInfo = ((ResponseDeviceInfo) reply).getDeviceInfo();
		assertNotNull(deviceInfo);
		assertEquals(true, deviceInfo.getRegistered());
	}

}
