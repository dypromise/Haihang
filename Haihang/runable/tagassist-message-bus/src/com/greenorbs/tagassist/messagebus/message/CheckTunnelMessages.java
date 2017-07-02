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

public class CheckTunnelMessages {
	
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
	
	public static class CarriageChanged extends MessageBase {
		
		private String _carriageId;
		
		private boolean _bound;

		public String getCarriageId() {
			return _carriageId;
		}

		public void setCarriageId(String carriageId) {
			_carriageId = carriageId;
		}

		public boolean getBound() {
			return _bound;
		}

		public void setBound(boolean bound) {
			_bound = bound;
		}
		
	}
	
	public static class LocationMoved extends MessageBase {
		
		private String _locationParam1;
		
		private Integer _locationParam2;
		
		private Float _locationParam3;

		public String getLocationParam1() {
			return _locationParam1;
		}

		public void setLocationParam1(String locationParam1) {
			_locationParam1 = locationParam1;
		}

		public Integer getLocationParam2() {
			return _locationParam2;
		}

		public void setLocationParam2(Integer locationParam2) {
			_locationParam2 = locationParam2;
		}

		public Float getLocationParam3() {
			return _locationParam3;
		}

		public void setLocationParam3(Float locationParam3) {
			_locationParam3 = locationParam3;
		}
		
	}
	
	public static abstract class BaggageChecked extends MessageBase {
		
		public static final int DIRECTION_IN	= 1;
		public static final int DIRECTION_OUT	= 2;
		
		private String _epc;
		
		private int _direction;
		
		private String _carriageId;
		
		private Float _rssi;
		
		private boolean _wasCheckedInRight;
		
		private String _operator;
		
		public String getEPC() {
			return _epc;
		}

		public void setEPC(String epc) {
			_epc = epc;
		}

		public int getDirection() {
			return _direction;
		}

		public void setDirection(int direction) {
			_direction = direction;
		}
		
		public String getCarriageId() {
			return _carriageId;
		}

		public void setCarriageId(String carriageId) {
			_carriageId = carriageId;
		}

		public Float getRssi() {
			return _rssi;
		}

		public void setRssi(Float rssi) {
			_rssi = rssi;
		}
		
		public boolean getWasCheckedInRight() {
			return _wasCheckedInRight;
		}

		public void setWasCheckedInRight(boolean wasCheckedInRight) {
			_wasCheckedInRight = wasCheckedInRight;
		}

		public String getOperator() {
			return _operator;
		}

		public void setOperator(String operator) {
			_operator = operator;
		}
		
	}
	
	public static class BaggageCheckedIn extends BaggageChecked {
		
		public BaggageCheckedIn() {
			this.setDirection(DIRECTION_IN);
		}

	}
	
	public static class BaggageCheckedOut extends BaggageChecked {
		
		public BaggageCheckedOut() {
			this.setDirection(DIRECTION_OUT);
		}

	}

	public static class BaggageDamaged extends MessageBase {
		
		private String _baggageNumber;
		
		private int _damageCode;
		
		private String _operator;
		
		public String getBaggageNumber() {
			return _baggageNumber;
		}

		public void setBaggageNumber(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}

		public int getDamageCode() {
			return _damageCode;
		}

		public void setDamageCode(int damageCode) {
			_damageCode = damageCode;
		}

		public String getOperator() {
			return _operator;
		}

		public void setOperator(String operator) {
			_operator = operator;
		}
		
	}

}
