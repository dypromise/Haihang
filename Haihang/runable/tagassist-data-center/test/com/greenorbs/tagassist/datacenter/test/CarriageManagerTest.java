package com.greenorbs.tagassist.datacenter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.CarriageRegister;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.CarriageUnregister;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.CarriageUpdate;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCarriageInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCarriageInfo;

public class CarriageManagerTest {

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
		String number = "Carriage#1";
		
		CarriageInfo carriageInfo = new CarriageInfo();
		carriageInfo.setUUID(uuid);
		carriageInfo.setNumber(number);
		
		// Create
		AbstractMessage command = new CarriageRegister(carriageInfo);
		Confirmation confirmation = (Confirmation) _publisher.queryOne(command);
		assertNotNull(confirmation);
		assertEquals(Confirmation.RESULT_SUCCESS, confirmation.getResult());
		
		// Validate creation
		QueryCarriageInfo query = new QueryCarriageInfo(uuid);
		ResponseCarriageInfo response = (ResponseCarriageInfo) _publisher.queryOne(query);
		assertNotNull(response);
		assertEquals(number, response.getCarriageInfo().getNumber());
		assertEquals(Integer.valueOf(CarriageInfo.STATUS_UNKNOWN), response.getCarriageInfo().getStatus());
		
		// Setup for modification
		number = "Carriage#2";
		String destination = "SYX";
		carriageInfo.setNumber(number);
		carriageInfo.setDestination("SYX");
		carriageInfo.setStatus(CarriageInfo.STATUS_IN_USE);
		
		// Modify
		command = new CarriageUpdate(carriageInfo);
		confirmation = (Confirmation) _publisher.queryOne(command);
		assertNotNull(confirmation);
		assertEquals(Confirmation.RESULT_SUCCESS, confirmation.getResult());
		
		// Validate modification
		response = (ResponseCarriageInfo) _publisher.queryOne(query);
		assertNotNull(response);
		assertEquals(number, response.getCarriageInfo().getNumber());
		assertEquals(destination, response.getCarriageInfo().getDestination());
		assertEquals(Integer.valueOf(CarriageInfo.STATUS_IN_USE), response.getCarriageInfo().getStatus());
		
		// Delete
		command = new CarriageUnregister(uuid);
		confirmation = (Confirmation) _publisher.queryOne(command);
		assertNotNull(confirmation);
		assertEquals(Confirmation.RESULT_SUCCESS, confirmation.getResult());
		
		// Validate Deletion
		response = (ResponseCarriageInfo) _publisher.queryOne(query);
		assertNotNull(response);
		assertNull(response.getCarriageInfo());
	}

}
