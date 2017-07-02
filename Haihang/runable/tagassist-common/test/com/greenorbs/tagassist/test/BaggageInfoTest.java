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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.BaggageInfo;

public class BaggageInfoTest implements PropertyChangeListener {

	BaggageInfo bag = new BaggageInfo();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
	public void set(){
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_ID, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_BCLASS, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_DAMAGE_CODE, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_EPC, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_FLIGHT_ID, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_LAST_TRACED_DEVICE, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_LAST_TRACED_TIME, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_NUMBER, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_STATUS, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPERTY_WEIGHT, this);
		bag.addPropertyChangeListener(BaggageInfo.PROPRETY_PASSENGER, this);
		
	}

	@Test
	public void testGetId() {
		bag.setId(123);

		assertEquals(bag.getId(), new Integer(123));
	}

	@Test
	public void testGetNumber() {

		bag.setNumber("1234");

		assertEquals(bag.getNumber(), "1234");
	}

	@Test
	public void testGetEPC() {
		bag.setEPC("ABC");
		assertEquals(bag.getEPC(), "ABC");
	}

	@Test
	public void testGetFlightId() {
		bag.setFlightId("HU123");
		assertEquals(bag.getFlightId(),"HU123");
	}

	@Test
	public void testGetPassenger() {
		bag.setPassenger("passenger");
		assertEquals(bag.getPassenger(),"passenger");
	}

	@Test
	public void testGetWeight() {
		bag.setWeight(new Integer(123));
		assertEquals(bag.getWeight(), new Integer(123));
	}

	@Test
	public void testGetBClass() {
		bag.setBClass("ABC");
		assertEquals(bag.getBClass(), "ABC");
	}

	@Test
	public void testGetDamageCode() {
		bag.setDamageCode(new Integer(BaggageInfo.DAMAGE_CRUSH));
		assertEquals(bag.getDamageCode(), new Integer(BaggageInfo.DAMAGE_CRUSH));
	}

	@Test
	public void testGetStatus() {
		bag.setStatus(0);
		assertEquals(bag.getStatus(), new Integer(0));
	}


	@Test
	public void testGetLastCheckTunnelId() {
		bag.setLastTracedDevice("123");
		assertEquals(bag.getLastTracedDevice(),"123");
	}

	@Test
	public void testGetLastCheckedTime() {
		bag.setLastTracedTime(new Long(12345));
		assertEquals(bag.getLastTracedTime(), new Long(12345));
	}

	@Test
	public void testClone() {
		BaggageInfo bag = new BaggageInfo();
		bag.setNumber("123456");

		try {
			BaggageInfo bag2 = (BaggageInfo) bag.clone();
			bag2.setNumber("234567");
			assertEquals(bag.getNumber(), "123456");
			assertEquals(bag2.getNumber(),"234567");
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		System.out.println(evt.getPropertyName());

	}

}
