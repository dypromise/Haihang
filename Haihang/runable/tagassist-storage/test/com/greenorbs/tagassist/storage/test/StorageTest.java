/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 22, 2012
 */

package com.greenorbs.tagassist.storage.test;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import com.greenorbs.tagassist.storage.query.StorageFactory;

public class StorageTest {

	@Test
	public void test() {
		
		
		Assert.assertNotNull(StorageFactory.getBaggageStorage());
		Assert.assertNotNull(StorageFactory.getCheckTunnelStorage());
		Assert.assertNotNull(StorageFactory.getFlightStorage());
		Assert.assertNotNull(StorageFactory.getMobileReaderStorage());
		Assert.assertNotNull(StorageFactory.getNotificationStorage());
		Assert.assertNotNull(StorageFactory.getTrackTunnelStorage());
		Assert.assertNotNull(StorageFactory.getWristbandStorage());

	}

}
