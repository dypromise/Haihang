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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.TrackTunnelInfo;

public class TrackTunnelInfoTest implements PropertyChangeListener{
	
	TrackTunnelInfo trackTunnel = new TrackTunnelInfo();
	
	static int count = 0;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		this.trackTunnel.addPropertyChangeListener(TrackTunnelInfo.PROPERTY_DEVICE_INFO, this);
		this.trackTunnel.addPropertyChangeListener(TrackTunnelInfo.PROPERTY_LOCATION_PARAM_1, this);
		this.trackTunnel.addPropertyChangeListener(TrackTunnelInfo.PROPERTY_LOCATION_PARAM_2, this);
		this.trackTunnel.addPropertyChangeListener(TrackTunnelInfo.PROPERTY_UUID, this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetUUID() {
		trackTunnel.setUUID("12345");
		assertEquals(trackTunnel.getUUID(),"12345");
	}

	@Test
	public void testGetDeviceInfo() {
		
		DeviceInfo device = new DeviceInfo();
		device.setUUID("123");
		this.trackTunnel.setDeviceInfo(device);
		
		assertEquals(this.trackTunnel.getDeviceInfo(), device);
		
	}

	@Test
	public void testGetLocationParam1() {
		
		this.trackTunnel.setLocationParam1("abc");
		assertEquals(this.trackTunnel.getLocationParam1(), "abc");
	}

	@Test
	public void testGetLocationParam2() {
		
		this.trackTunnel.setLocationParam2(new Float(123.45));
		
		assertEquals(this.trackTunnel.getLocationParam2(), new Float(123.45));
	}

	@Test
	public void testClone() throws CloneNotSupportedException {
		
		this.trackTunnel.setDeviceInfo(new DeviceInfo());
		this.trackTunnel.getDeviceInfo().setName("anc");
		TrackTunnelInfo t2 = (TrackTunnelInfo) this.trackTunnel.clone();
		t2.getDeviceInfo().setName("ddd");
		assertEquals(this.trackTunnel.getDeviceInfo().getName(),"anc");
		assertEquals(t2.getDeviceInfo().getName(),"ddd");
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		count++;
		
		System.out.println(evt.getPropertyName());
	}

}
