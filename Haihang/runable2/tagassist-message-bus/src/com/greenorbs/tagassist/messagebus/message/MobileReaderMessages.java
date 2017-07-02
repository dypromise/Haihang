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

public class MobileReaderMessages {
	
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
	
	public static class DataLoaded extends MessageBase {
		
		private String _flightId;

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}
	
	public static class BoardingReport extends MessageBase {
		
		private String _flightId;
		
		private int _expectedBaggageSize;
		
		private int _boardedBaggageSize;
		
		private String[] _boardedBaggageNumberList;
		
		private String[] _unexpectedBaggageEPCList;
		
		private String _operator;

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}

		public int getExpectedBaggageSize() {
			return _expectedBaggageSize;
		}

		public void setExpectedBaggageSize(int expectedBaggageSize) {
			_expectedBaggageSize = expectedBaggageSize;
		}

		public int getBoardedBaggageSize() {
			return _boardedBaggageSize;
		}

		public void setBoardedBaggageSize(int boardedBaggageSize) {
			_boardedBaggageSize = boardedBaggageSize;
		}

		public String[] getBoardedBaggageNumberList() {
			return _boardedBaggageNumberList;
		}

		public void setBoardedBaggageNumberList(String[] boardedBaggageNumberList) {
			_boardedBaggageNumberList = boardedBaggageNumberList;
		}

		public String[] getUnexpectedBaggageEPCList() {
			return _unexpectedBaggageEPCList;
		}

		public void setUnexpectedBaggageEPCList(String[] unexpectedBaggageEPCList) {
			_unexpectedBaggageEPCList = unexpectedBaggageEPCList;
		}

		public String getOperator() {
			return _operator;
		}

		public void setOperator(String operator) {
			_operator = operator;
		}
		
	}

}
