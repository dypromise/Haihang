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
import com.greenorbs.tagassist.WristbandInfo;

public class WristbandInfoTest {

	WristbandInfo wristband = new WristbandInfo();
	
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
		this.wristband.setUUID("122");
		assertEquals(this.wristband.getUUID(),"122");
	}

	@Test
	public void testGetDeviceInfo() {
		this.wristband.setDeviceInfo(new DeviceInfo());
		this.wristband.getDeviceInfo().setName("name");
		assertEquals(this.wristband.getDeviceInfo().getName(),"name");
	}

	@Test
	public void testGetSubStatus() {
		this.wristband.setSubStatus(Boolean.TRUE);
		assertEquals(this.wristband.getSubStatus(), Boolean.TRUE);
	}

	@Test
	public void testGetBattery() {
		this.wristband.setBattery(new Integer(10));
		assertEquals(this.wristband.getBattery(),new Integer(10));
	}

	@Test
	public void testGetRFIDStatus() {
		this.wristband.setRFIDStatus(Boolean.TRUE);
		assertEquals(this.wristband.getRFIDStatus(), Boolean.TRUE);
	}

	@Test
	public void testGetBoundCheckTunnelIdList() {
		this.wristband.setBoundFlightIdList(new String[] {"CP161/20NOV2012"});
		assertEquals(this.wristband.getBoundFlightIdList()[0], "CP161/20NOV2012");
	}

}
