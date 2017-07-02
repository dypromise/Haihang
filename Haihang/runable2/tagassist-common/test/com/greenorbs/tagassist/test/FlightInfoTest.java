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
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.FlightInfo;

public class FlightInfoTest {

	FlightInfo flight = new FlightInfo();
	
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
	public void testGetId() {
		this.flight.setId(new Integer(10));
		assertEquals(this.flight.getId(), new Integer(10));
	}

	@Test
	public void testGetFlightId() {
		this.flight.setFlightId("HU123");
		assertEquals(this.flight.getFlightId(), "HU123");
	}


	@Test
	public void testGetDepartTime() {
		this.flight.setDepartTime(new Long(12334));
		assertEquals(this.flight.getDepartTime(), new Long(12334));
	}
	
	@Test
	public void testGetArriveTime() {
		this.flight.setArriveTime(new Long(123));
		assertEquals(this.flight.getArriveTime(), new Long(123));
	}

	@Test
	public void testGetStatus() {
		this.flight.setStatus(FlightInfo.STATUS_CHECKING_IN);
		assertEquals(this.flight.getStatus(), new Integer(FlightInfo.STATUS_CHECKING_IN));
	}


	@Test
	public void testEqualsObject() {
		this.flight.setFlightId("1234");
		FlightInfo f = new FlightInfo();
		f.setFlightId("1234");
		assertEquals(this.flight, f);
	}
	
	@Test
	public void testClone() throws CloneNotSupportedException {
		FlightInfo f = (FlightInfo) this.flight.clone();
		
		f.setArriveTime(new Long(1234));
		
		Assert.assertTrue(f.getArriveTime().equals(this.flight.getArriveTime())==false);
		
	}

}
