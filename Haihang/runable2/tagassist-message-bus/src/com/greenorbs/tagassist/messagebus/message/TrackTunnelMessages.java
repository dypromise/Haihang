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

import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;

public class TrackTunnelMessages {
	
	public static class LocationMoved extends MessageBase {
		
		private String _locationParam1;
		
		private Float _locationParam2;
		
		public String getLocationParam1() {
			return _locationParam1;
		}

		public void setLocationParam1(String locationParam1) {
			_locationParam1 = locationParam1;
		}

		public Float getLocationParam2() {
			return _locationParam2;
		}

		public void setLocationParam2(Float locationParam2) {
			_locationParam2 = locationParam2;
		}

	}

	public static class BaggageTracked extends MessageBase {
		
		private String _epc;
		
		private String _poolId;
		
		private float _distance;
		
		private Float _rssi;
		
		public String getEPC() {
			return _epc;
		}

		public void setEPC(String epc) {
			_epc = epc;
		}

		public String getPoolId() {
			return _poolId;
		}

		public void setPoolId(String poolId) {
			_poolId = poolId;
		}

		public float getDistance() {
			return _distance;
		}

		public void setDistance(float location) {
			_distance = location;
		}
		
		public Float getRssi() {
			return _rssi;
		}

		public void setRssi(Float rssi) {
			_rssi = rssi;
		}
		
	}
	
	public static class BaggageTrackingLost extends MessageBase {
		
		private String _epc;
		
		private String _poolId;

		public String getEpc() {
			return _epc;
		}

		public void setEpc(String epc) {
			_epc = epc;
		}

		public String getPoolId() {
			return _poolId;
		}

		public void setPoolId(String poolId) {
			_poolId = poolId;
		}
		
	}
	
	public static class TrackTunnelInfoReport extends HardwareInfoReport {
		
		private String _locationParam1;
		
		private Float _locationParam2;

		public String getLocationParam1() {
			return _locationParam1;
		}

		public void setLocationParam1(String locationParam1) {
			_locationParam1 = locationParam1;
		}

		public Float getLocationParam2() {
			return _locationParam2;
		}

		public void setLocationParam2(Float locationParam2) {
			_locationParam2 = locationParam2;
		}
		
	}
	
}
