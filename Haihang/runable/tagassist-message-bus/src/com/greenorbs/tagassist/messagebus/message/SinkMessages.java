/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 12, 2012
 */

package com.greenorbs.tagassist.messagebus.message;

public class SinkMessages {

	public static class WristbandRegistered extends MessageBase {
		
		private String _wristbandId;
		
		private String _name;

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}

		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}
		
	}
	
	public static class WristbandUnregistered extends MessageBase {
		
		private String _wristbandId;
		
		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}

	}

}
