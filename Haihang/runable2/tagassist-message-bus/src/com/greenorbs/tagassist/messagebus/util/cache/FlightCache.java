/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 15, 2012
 */

package com.greenorbs.tagassist.messagebus.util.cache;

import java.util.Collection;
import java.util.Hashtable;

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.util.querier.FlightQuerier;

public class FlightCache {

	private Hashtable<String, FlightInfo> _cache;
	private FlightQuerier _flightQuerier;
	
	public FlightCache(Publisher publisher) {
		_cache = new Hashtable<String, FlightInfo>();
		_flightQuerier = new FlightQuerier(publisher);
	}
	
	public synchronized void initialize() {
		FlightInfo[] flightInfoList = _flightQuerier.getFlightList();
		if (flightInfoList != null) {
			for (FlightInfo flightInfo : flightInfoList) {
				String flightId = flightInfo.getFlightId();
				_cache.put(flightId, flightInfo);
			}
		}
	}
	
	public synchronized void update(String flightId) {
		FlightInfo flightInfo = _flightQuerier.getFlightInfo(flightId);
		if (flightInfo != null) {
			this.put(flightInfo);
		}
	}

	public synchronized Collection<FlightInfo> flights() {
		return _cache.values();
	}
	
	public synchronized void remove(String flightId) {
		_cache.remove(flightId);
	}
	
	public synchronized boolean contains(String flightId) {
		return _cache.containsKey(flightId);
	}
	
	public synchronized FlightInfo get(String flightId) {
		return _cache.get(flightId);
	}
	
	public synchronized void put(FlightInfo flightInfo) {
		_cache.put(flightInfo.getFlightId(), flightInfo);
	}
	
}
