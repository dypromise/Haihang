/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-5
 */

package com.greenorbs.tagassist.test;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.DeviceInfo;

public class CheckTunnelInfoTest {

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
		CheckTunnelInfo checkTunnel = new CheckTunnelInfo();
		UUID uuid = UUID.randomUUID();
		checkTunnel.setUUID(uuid.toString());
		assertEquals(checkTunnel.getUUID(), uuid.toString());
	}

	@Test
	public void testGetDeviceInfo() {
		try {

			CheckTunnelInfo checktunnel = new CheckTunnelInfo();
			DeviceInfo device = new DeviceInfo();
			device.setUUID("123");
			checktunnel.setDeviceInfo(device);
			assertEquals(checktunnel.getDeviceInfo(), device);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetBoundFlightId() {

		CheckTunnelInfo checkTunnel = new CheckTunnelInfo();
		checkTunnel.setBoundFlightId("123");
		assertEquals(checkTunnel.getBoundFlightId(), "123");
	}

	@Test
	public void testGetLocationParam1() {

		CheckTunnelInfo checkTunnel = new CheckTunnelInfo();
		checkTunnel.setLocationParam1("123");
		assertEquals(checkTunnel.getLocationParam1(), "123");
	}

	@Test
	public void testGetLocationParam2() {
		CheckTunnelInfo checkTunnel = new CheckTunnelInfo();
		checkTunnel.setLocationParam2(Integer.valueOf(234));
		assertEquals(checkTunnel.getLocationParam2(), Integer.valueOf(234));
	}

	@Test
	public void testSetLocationParam3() {
		CheckTunnelInfo checkTunnel = new CheckTunnelInfo();
		checkTunnel.setLocationParam3(new Float(3.0));
		assertEquals(checkTunnel.getLocationParam3(), new Float(3.0));
	}

	@Test
	public void testClone() throws CloneNotSupportedException {

		CheckTunnelInfo c = new CheckTunnelInfo();
		DeviceInfo device = new DeviceInfo();
		UUID uuid = UUID.randomUUID();
		c.setDeviceInfo(device);
		c.setUUID(uuid.toString());
		c.getDeviceInfo().setName("234");

		CheckTunnelInfo c2 = (CheckTunnelInfo) c.clone();
		c2.getDeviceInfo().setName("123");

		System.out.println(c.getDeviceInfo().getName());
		System.out.println(c2.getDeviceInfo().getName());
		assertEquals(c.getDeviceInfo().getName(), "234");
		assertEquals(c2.getDeviceInfo().getName(), "123");
	}
	
	@Test
	public void testPropertyChange(){
		CheckTunnelInfo c = new CheckTunnelInfo();
		DeviceInfo device = new DeviceInfo();
		c.setDeviceInfo(device);
		
		c.getDeviceInfo().addPropertyChangeListener(DeviceInfo.PROPERTY_NAME, new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				System.out.println(evt.getPropertyName()+":"+evt.getNewValue());
				
			}
			
		});
		
		c.getDeviceInfo().setName("name");
		
	}

}
