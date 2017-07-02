/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-22
 */

package com.greenorbs.tagassist.storage.memory;

import java.util.Collection;
import java.util.Iterator;

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.storage.FlightStorage;

public class MemoryFlightStorage extends FlightStorage {

	private MemoryStorageHelper<FlightInfo> _helper;

	public MemoryFlightStorage() {

		this._helper = new MemoryStorageHelper<FlightInfo>(this);
	}

	@Override
	public synchronized int size() {
		return this._helper.size();
	}

	@Override
	public synchronized boolean isEmpty() {
		return this._helper.isEmpty();
	}

	@Override
	public synchronized boolean contains(Object o) {
		if (o instanceof String) {
			for (FlightInfo f : this) {
				if (f.getFlightId().equals(o)) {
					return true;
				}
			}
		}
		return this._helper.contains(o);

	}

	@Override
	public synchronized Iterator<FlightInfo> iterator() {
		return this._helper.iterator();
	}

	@Override
	public synchronized Object[] toArray() {
		return this._helper.toArray();
	}

	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return this._helper.toArray(a);
	}

	@Override
	public synchronized boolean addWithoutFireEvent(FlightInfo e) {
		return this._helper.add(e);
	}

	@Override
	public synchronized boolean removeWithoutFireEvent(Object o) {

		return this._helper.remove(o);
	}

	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		return this._helper.containsAll(c);
	}

	/* ======FlightStorage Interface========= */

	/**
	 * Find by the flight Id
	 */
	@Override
	public synchronized FlightInfo findByFlightId(String flightId) {

		Iterator<FlightInfo> it = this.iterator();
		while (it.hasNext()) {
			FlightInfo f = it.next();
			if (f.getFlightId() != null && f.getFlightId().equals(flightId)) {
				return f;
			}
		}

		return null;
	}

}
