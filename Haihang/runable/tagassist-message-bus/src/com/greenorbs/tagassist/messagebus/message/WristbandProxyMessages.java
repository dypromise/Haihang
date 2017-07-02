/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.message;

import com.greenorbs.tagassist.WristbandInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;

public class WristbandProxyMessages {
	
	public static class BindingChanged extends MessageBase {
		
		private String _flightId;
		
		private boolean _bound;

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}

		public boolean getBound() {
			return _bound;
		}

		public void setBound(boolean bound) {
			_bound = bound;
		}
		
	}
	
	public static class BaggageIdentified extends MessageBase {
		
		private String _epc;
		
		private Float _rssi;
		
		private String _operator;

		public String getEPC() {
			return _epc;
		}

		public void setEPC(String epc) {
			_epc = epc;
		}
		
		public Float getRssi() {
			return _rssi;
		}

		public void setRssi(Float rssi) {
			_rssi = rssi;
		}

		public String getOperator() {
			return _operator;
		}

		public void setOperator(String operator) {
			_operator = operator;
		}
		
	}
	
	public static class WristbandInfoReport extends HardwareInfoReport {
		
		private WristbandInfo _wristbandInfo;
		
		public WristbandInfo getWristbandInfo() {
			return _wristbandInfo;
		}

		public void setWristbandInfo(WristbandInfo wristbandInfo) {
			_wristbandInfo = wristbandInfo;
		}
		
	}
	
}
