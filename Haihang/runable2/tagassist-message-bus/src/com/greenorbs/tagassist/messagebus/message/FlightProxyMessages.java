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

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;

public class FlightProxyMessages {

	public static class BaggageArrival extends MessageBase {
		
		private BaggageInfo _baggageInfo;
		
		public BaggageArrival() {}
		
		public BaggageArrival(BaggageInfo baggageInfo) {
			_baggageInfo = baggageInfo;
		}

		public BaggageInfo getBaggageInfo() {
			return _baggageInfo;
		}

		public void setBaggageInfo(BaggageInfo baggageInfo) {
			_baggageInfo = baggageInfo;
		}
		
	}
	
	public static class BaggageRemoval extends MessageBase {
		
		private BaggageInfo _baggageInfo;
		
		private int _oldStatus;
		
		public BaggageRemoval() {}
		
		public BaggageRemoval(BaggageInfo baggageInfo, int oldStatus) {
			_baggageInfo = baggageInfo;
			_oldStatus = oldStatus;
		}

		public BaggageInfo getBaggageInfo() {
			return _baggageInfo;
		}

		public void setBaggageInfo(BaggageInfo baggageInfo) {
			_baggageInfo = baggageInfo;
		}

		public int getOldStatus() {
			return _oldStatus;
		}

		public void setOldStatus(int oldStatus) {
			_oldStatus = oldStatus;
		}
		
	}
	
	public static class FlightScheduled extends MessageBase {
		
		private FlightInfo _flightInfo;
		
		public FlightScheduled() {}
		
		public FlightScheduled(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}

		public FlightInfo getFlightInfo() {
			return _flightInfo;
		}

		public void setFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
	}
	
	public static class FlightRescheduled extends MessageBase {
		
		private FlightInfo _flightInfo;
		
		public FlightRescheduled() {}
		
		public FlightRescheduled(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
		public FlightInfo getFlightInfo() {
			return _flightInfo;
		}

		public void setFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
	}
	
	@Deprecated
	public static class FlightDelayed extends FlightRescheduled {}
	
	public static class FlightCanceled extends MessageBase {
		
		private FlightInfo _flightInfo;

		public FlightCanceled() {}
		
		public FlightCanceled(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
		public FlightInfo getFlightInfo() {
			return _flightInfo;
		}

		public void setFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
	}

	public static class FlightCheckingIn extends MessageBase {
		
		private FlightInfo _flightInfo;

		public FlightCheckingIn() {}
		
		public FlightCheckingIn(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
		public FlightInfo getFlightInfo() {
			return _flightInfo;
		}

		public void setFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
	}
	
	public static class FlightClosed extends MessageBase {
		
		private FlightInfo _flightInfo;

		public FlightClosed() {}
		
		public FlightClosed(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
		public FlightInfo getFlightInfo() {
			return _flightInfo;
		}

		public void setFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
	}
	
	public static class FlightDepartured extends MessageBase {
		
		private FlightInfo _flightInfo;

		public FlightDepartured() {}
		
		public FlightDepartured(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
		public FlightInfo getFlightInfo() {
			return _flightInfo;
		}

		public void setFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
	}
	
}
