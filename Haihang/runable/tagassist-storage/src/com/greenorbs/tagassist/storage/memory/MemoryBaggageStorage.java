/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Author: Lei Yang, 2012-2-22
 */

package com.greenorbs.tagassist.storage.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CompositeMap;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.storage.BaggageStorage;

public class MemoryBaggageStorage extends BaggageStorage {

	// <FlightId, <BaggageNumber, BaggeInfo>>
	private Map<String, Map<String, BaggageInfo>> _baggages;

	private static Logger _log = Logger.getLogger(MemoryBaggageStorage.class);

	public MemoryBaggageStorage() {

		this._baggages = Collections
				.synchronizedMap(new HashMap<String, Map<String, BaggageInfo>>());
	}

	private synchronized Map<String, BaggageInfo> compositeMap() {
		CompositeMap maps = new CompositeMap();
		try {

			for (Map<String, BaggageInfo> map : this._baggages.values()) {
				maps.addComposited(map);
			}
		} catch (IllegalArgumentException e) {
			_log.error("The same baggage happens to be in two different flights. This violate the rule. "
					+ "The repeated baggage object will be discarded from the storage.");
		}
		return maps;
	}

	@Override
	public synchronized boolean addWithoutFireEvent(BaggageInfo item) {
		if (item == null) {
			return false;
		}

		try {
			if (this._baggages.containsKey(item.getFlightId()) == false) {
				this._baggages.put(item.getFlightId(),
						new HashMap<String, BaggageInfo>());
			}

			this._baggages.get(item.getFlightId()).put(item.getNumber(), item);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("It fails to add bagage into MemoryBaggageStorage." + e);
			return false;
		}

		return true;
	}

	@Override
	public synchronized boolean removeWithoutFireEvent(Object item) {

		if (item == null) {
			return false;
		}

		try {
			if (item instanceof BaggageInfo) {
				BaggageInfo bag = (BaggageInfo) item;
				Map<String, BaggageInfo> map = this._baggages.get(bag
						.getFlightId());
				if (map != null) {
					map.remove(bag.getNumber());
					if (map.isEmpty()) {
						this._baggages.remove(bag.getFlightId());
					}
				}
			}
		} catch (Exception e) {
			_log.error("It fails to remove baggage from the MemoryBaggageStorage with exception:"
					+ e);
			return false;
		}

		return true;

	}

	@Override
	public synchronized int size() {

		return this.compositeMap().size();

	}

	@Override
	public synchronized Iterator<BaggageInfo> iterator() {

		return compositeMap().values().iterator();
	}

	@Override
	public synchronized boolean isEmpty() {
		return compositeMap().isEmpty();
	}

	@Override
	public synchronized boolean contains(Object o) {

		if (o instanceof String) {
			return compositeMap().containsKey(o);
		}

		return compositeMap().values().contains(o);
	}

	@Override
	public synchronized Object[] toArray() {
		return compositeMap().values().toArray();
	}

	@Override
	public synchronized <T> T[] toArray(T[] a) {

		return (T[]) compositeMap().values().toArray(a);
	}

	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		return compositeMap().values().containsAll(c);
	}

	/**************** BaggageStorage interface ****************/

	@Override
	public synchronized List<BaggageInfo> findByFlightId(String flightId) {

		List<BaggageInfo> result = new ArrayList<BaggageInfo>();

		if (StringUtils.isBlank(flightId)) {
			return result;
		}

		if (this._baggages.containsKey(flightId)) {
			for (BaggageInfo bag : this._baggages.get(flightId).values()) {
				result.add(bag);
			}
		}

		return result;

	}

	@Override
	public synchronized BaggageInfo findByEPC(String EPC) {

		Iterator<BaggageInfo> it = this.iterator();
		BaggageInfo bag = null;

		while (it.hasNext()) {
			bag = it.next();
			if (bag.getEPC().equals(EPC)) {
				return bag;
			}
		}

		return null;
	}

	@Override
	public synchronized List<BaggageInfo> findByStatus(String flightId,
			int status) {

		List<BaggageInfo> result = new ArrayList<BaggageInfo>();

		if (this._baggages.containsKey(flightId)) {
			for (BaggageInfo bag : this._baggages.get(flightId).values()) {
				if (bag.getStatus() == status) {

					result.add(bag);

				}
			}
		}
		return result;
	}

	@Override
	public synchronized BaggageInfo findByNumber(String number) {
		if (StringUtils.isBlank(number)) {
			return null;
		}

		BaggageInfo bag = compositeMap().get(number);

		return bag;
	}

	@Override
	public synchronized BaggageInfo findByEPC(String flightId, String EPC) {

		if (this._baggages.containsKey(flightId)) {
			for (BaggageInfo bag : this._baggages.get(flightId).values()) {
				if (bag.getEPC().equals(EPC)) {
					return bag;
				}
			}
		}
		return null;
	}

	@Override
	public synchronized boolean removeByFlightId(String flightId) {

		if (StringUtils.isBlank(flightId)) {
			return false;
		}

		Iterator<BaggageInfo> bagIt = this.iterator();
		while (bagIt.hasNext()) {
			BaggageInfo item = bagIt.next();
			if (item.getFlightId().equals(flightId)) {
				bagIt.remove();
			}
		}

		return true;
	}

}
