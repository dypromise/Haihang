/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-12
 */

package com.greenorbs.tagassist.util.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.util.BeiyangCRC;

public class BeiyangCRCTest {

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
	public void test() {

		try {
			byte[] data1 = new byte[] { 0x00, 0x18, (byte) 0xFF, (byte) 0xFF, (byte) 0x84, 0x20,
					0x54, 0x00, (byte) 0xE2, 0x00, 0x68, (byte) 0x82, (byte) 0x82, 0x16, 0x02,
					0x55, 0x14, 0x10, (byte) 0x88, 0x4B, 0x00, 0x00, 0x00, (byte) 0xE3, 0x01, 0x04 };

			BeiyangCRC.printHex(data1);
			BeiyangCRC.printHex(BeiyangCRC.getCRC16(data1));

			assertArrayEquals(BeiyangCRC.getCRC16(data1), new byte[] { (byte) 0xAD, (byte) 0xE2 });

			byte[] data2 = new byte[] {  0x00, 0x18, (byte) 0xff, (byte) 0xff, (byte) 0x84, 0x20, 0x56, 0x00,
					(byte) 0xe2, 0x00, 0x68, (byte) 0x82, (byte) 0x82, 0x16, 0x02, 0x55, 0x14,
					0x10, (byte) 0x88, 0x4b, 0x00, 0x00, 0x00, (byte) 0xe7, 0x01, 0x04 };
			assertArrayEquals(BeiyangCRC.getCRC16(data2), new byte[] { 0x78, 0x3E });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
