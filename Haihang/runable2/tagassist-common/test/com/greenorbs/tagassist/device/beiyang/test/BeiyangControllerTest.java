/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-10
 */

package com.greenorbs.tagassist.device.beiyang.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.device.ICautionLight;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.ISpeaker;
import com.greenorbs.tagassist.device.beiyang.BeiyangPeripheral;

public class BeiyangControllerTest {

	static BeiyangPeripheral _controller;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		_controller = new BeiyangPeripheral(6001,"192.168.50.198",4001);
		_controller.startup();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		_controller.shutdown();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStatus() {

		assertEquals(_controller.getStatus(), IHardware.STATUS_ON);

	}


	@Test
	public void testTurnOn() throws Exception {

		_controller.turnOn(ICautionLight.RED);

		_controller.turnOn(ICautionLight.GREEN);

		_controller.turnOn(ICautionLight.YELLOW);

		_controller.turnOff(ICautionLight.RED | ICautionLight.GREEN | ICautionLight.YELLOW);
	}

	@Test
	public void testSpeak() throws Exception {
		
		_controller.speak(ISpeaker.LONG_MODE);

		Thread.sleep(5000);

		_controller.speak(ISpeaker.SHORT_MODE);

		Thread.sleep(5000);

		_controller.mute();
	}


}
