/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.greenorbs.tagassist.messagebus.io.AbstractMessage;

public class AbstractMessageTest {
	
	TestMessage _message;
	
	@Before
	public void setUp() throws Exception {
		_message = new TestMessage();
	}
	
	@Test
	public void testFromJSON() {
		String json = "{\"booleanField\":false,\"intField\":100,\"stringField\":\"Test\"}";
		_message = (TestMessage) AbstractMessage.fromJSON(TestMessage.class.getName(), json);
		
		assertEquals(100, _message.getIntField());
		assertEquals("Test", _message.getStringField());
		assertEquals(false, _message.getBooleanField());
	}

	@Test
	public void testToJSON() {	
		_message.setIntField(100);
		_message.setStringField("Test");
		_message.setBooleanField(false);
		_message.setObjectField(null);
		
		String json = _message.toJSON();
		
		assertEquals("{\"booleanField\":false,\"intField\":100,\"stringField\":\"Test\"}", json);
	}

}
