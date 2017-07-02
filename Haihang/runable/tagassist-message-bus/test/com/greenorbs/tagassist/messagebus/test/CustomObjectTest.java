/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.messagebus.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages;

public class CustomObjectTest {
	
	public static class CustomObject {
		
		private int _poolId;
		
		private double _location;
		
		public CustomObject() {}
		
		public CustomObject(int poolId, double location) {
			_poolId = poolId;
			_location = location;
		}

		public int getPoolId() {
			return _poolId;
		}

		public void setPoolId(int poolId) {
			_poolId = poolId;
		}

		public double getLocation() {
			return _location;
		}

		public void setLocation(double location) {
			_location = location;
		}
		
	}
	
	@Test
	public void test() {
		GeneralMessages.HardwareInfoReport _message = new GeneralMessages.HardwareInfoReport();
		_message.setCustom(new CustomObject(5, 1.0));
		
		_message = (GeneralMessages.HardwareInfoReport) (AbstractMessage.fromJSON(
				GeneralMessages.HardwareInfoReport.class.getName(), _message.toJSON()));
		
		CustomObject custom = (CustomObject) _message.getCustom();
		
		assertEquals(5, custom.getPoolId());
		assertEquals(1.0, custom.getLocation(), 0);
	}

}
