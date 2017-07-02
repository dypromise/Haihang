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

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.util.querier.BaggageQuerier;

/**
 * 
 * This cache is built upon the assumption that flights are not too many.
 * 
 */
public class BaggageCache {
	
	private Hashtable<String, Hashtable<String, BaggageInfo>> _cache;
	private BaggageQuerier _baggageQuerier;
	
	public BaggageCache(Publisher publisher) {
		_cache = new Hashtable<String, Hashtable<String,BaggageInfo>>();
		_baggageQuerier = new BaggageQuerier(publisher);
	}
	
	public synchronized void initialize(Collection<FlightInfo> flightInfoList) {
		if (flightInfoList != null) {
			for (FlightInfo flightInfo : flightInfoList) {
				String flightId = flightInfo.getFlightId();
				_cache.put(flightId, new Hashtable<String, BaggageInfo>());
				
				BaggageInfo[] baggageInfoList = _baggageQuerier.getBaggageList(flightId);
				if (baggageInfoList != null) {
					Hashtable<String, BaggageInfo> t = _cache.get(flightId);
					for (BaggageInfo baggageInfo : baggageInfoList) {
						t.put(baggageInfo.getNumber(), baggageInfo);
					}
				}
			}
		}
	}
	
	public synchronized void update(String baggageNumber) {
		BaggageInfo baggageInfo = _baggageQuerier.getBaggageInfo(baggageNumber);
		if (baggageInfo != null) {
			this.put(baggageInfo);
		}
	}
	
	public synchronized void clear(String flightId) {
		_cache.remove(flightId);
	}
	
	public synchronized boolean contains(String baggageNumber) {
		for (Hashtable<String, BaggageInfo> t : _cache.values()) {
			if (t.containsKey(baggageNumber)) {
				return true;
			}
		}
		return false;
	}

	public synchronized BaggageInfo get(String baggageNumber) {
		for (Hashtable<String, BaggageInfo> t : _cache.values()) {
			if (t.containsKey(baggageNumber)) {
				return t.get(baggageNumber);
			}
		}
		return null;
	}
	
	public synchronized void put(BaggageInfo baggageInfo) {
		String flightId = baggageInfo.getFlightId();
		if (!_cache.containsKey(flightId)) {
			_cache.put(flightId, new Hashtable<String, BaggageInfo>());
		}
		_cache.get(flightId).put(baggageInfo.getNumber(), baggageInfo);
	}
	
}
