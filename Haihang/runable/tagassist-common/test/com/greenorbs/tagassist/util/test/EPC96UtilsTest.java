/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Mar 15, 2012
 */

package com.greenorbs.tagassist.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.greenorbs.tagassist.epc.EPC96;
import com.greenorbs.tagassist.util.EPC96Utils;

public class EPC96UtilsTest {

	@Test
	public void testIsValid() {
		assertTrue(EPC96Utils.isValid(EPC96.PREFIX_HEX + "0018234567FFF" + "319FC587FF"));
		assertFalse(EPC96Utils.isValid(EPC96.PREFIX_HEX + "0018234567000" + "319FC587FF"));
		assertFalse(EPC96Utils.isValid("0" + "0018234567FFF" + "319FC587FF"));
		assertFalse(EPC96Utils.isValid("0018234567FFF" + "319FC587FF"));
	}

}
