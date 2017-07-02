/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 13, 2012
 */

package com.greenorbs.tagassist.flightproxy.dcsadapter;

import java.util.ArrayList;

public class DCSMessage {
	
	public static final int TYPE_SOURCE 	= 1;
	public static final int TYPE_REMOVAL 	= 2;
	
	private int _type;
	private ArrayList<String> _baggageNumberList;
	private String _flightCode;
	private String _flightId;
	private String _destination;
	private String _bclass;
	private String _passenger;
	private int _baggageCount;
	private int _baggageWeight;
	
	public DCSMessage(int type) {
		_type = type;
		_baggageNumberList = new ArrayList<String>();
		_flightCode = null;
		_flightId = null;
		_destination = null;
		_bclass = null;
		_passenger = null;
		_baggageCount = 0;
		_baggageWeight = 0;
	}
	
	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	public ArrayList<String> getBaggageNumberList() {
		return _baggageNumberList;
	}

	public void setBaggageNumberList(ArrayList<String> baggageNumberList) {
		_baggageNumberList = baggageNumberList;
	}

	public String getFlightCode() {
		return _flightCode;
	}

	public void setFlightCode(String flightCode) {
		this._flightCode = flightCode;
	}

	public String getFlightId() {
		return _flightId;
	}

	public void setFlightId(String flightId) {
		_flightId = flightId;
	}

	public String getDestination() {
		return _destination;
	}

	public void setDestination(String destination) {
		_destination = destination;
	}

	public String getBClass() {
		return _bclass;
	}

	public void setBClass(String bclass) {
		_bclass = bclass;
	}

	public String getPassenger() {
		return _passenger;
	}

	public void setPassenger(String passenger) {
		_passenger = passenger;
	}
	
	public int getBaggageCount() {
		return _baggageCount;
	}

	public void setBaggageCount(int baggageCount) {
		_baggageCount = baggageCount;
	}

	public int getBaggageWeight() {
		return _baggageWeight;
	}

	public void setBaggageWeight(int baggageWeight) {
		_baggageWeight = baggageWeight;
	}

}
