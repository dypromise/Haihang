/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-11
 */

package com.greenorbs.tagassist.device.beiyang.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.beiyang.BeiyangTrackReader;
import com.greenorbs.tagassist.device.beiyang2.BeiyangInfraredRay;

public class BeiyangTrackReaderTest2 {
	
	

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
	public void testBeiyangTrackReader() throws HardwareException, InterruptedException {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"bean.xml");

		BeiyangTrackReader infraredray = (BeiyangTrackReader) ctx
				.getBean("trackReader");
		infraredray.startup();
		Thread.sleep(10000);
		infraredray.shutdown();
	}

}
