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

import org.junit.Test;

import com.greenorbs.tagassist.messagebus.io.MessageHelper;

public class MessageHelperTest {

	@Test
	public void testGetTopicName() {
		assertEquals(MessageHelper.getTopicName(new TestMessage()),
				MessageHelper.getTopicName(TestMessage.class));
	}

}
