/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-6
 */

package com.greenorbs.tagassist.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.DeviceInfo;

public class DeviceInfoTest {

	DeviceInfo device = new DeviceInfo();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGetUUID() {
		this.device.setUUID("123");
		assertEquals(this.device.getUUID(),"123");
	}


	@Test
	public void testGetComponent() {
		this.device.setComponent(1);
		assertEquals(this.device.getComponent(),new Integer(1));
	}



	@Test
	public void testGetName() {
		this.device.setName("name1");
		assertEquals(this.device.getName(),"name1");
	}

	

	@Test
	public void testGetStatus() {
		this.device.setStatus(2);
		assertEquals(this.device.getStatus(), new Integer(2));
	}

	

	@Test
	public void testGetRegistered() {
		this.device.setRegistered(Boolean.TRUE);
		assertEquals(this.device.getRegistered(), Boolean.TRUE);
	}

	
	@Test
	public void testGetRegistTime() {
		this.device.setRegistTime(new Long(12344));
		assertEquals(this.device.getRegistTime(), new Long(12344));
	}

	@Test
	public void testGetRemark() {
		this.device.setRemark("remarks");
		assertEquals(this.device.getRemark(),"remarks");
	}




	@Test
	public void testClone() throws CloneNotSupportedException {
		this.device.setName("2");
		DeviceInfo device = (DeviceInfo) this.device.clone();
		device.setName("i");
		
		assertTrue(this.device.getName().equals(device.getName())==false);
	}

}
