/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-28
 */

package com.greenorbs.tagassist.storage.memory;

import java.util.Collection;
import java.util.Iterator;

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.MobileReaderInfo;
import com.greenorbs.tagassist.storage.MobileReaderStorage;

public class MemoryMobileReaderStorage extends MobileReaderStorage {

	private MemoryStorageHelper<MobileReaderInfo> _helper = new MemoryStorageHelper<MobileReaderInfo>(
			this);

	@Override
	public synchronized MobileReaderInfo findByMobileId(String id) {
		for (MobileReaderInfo mobile : this._helper) {
			if (mobile.getUUID().equals(id)) {
				return mobile;
			}
		}
		return null;
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
			for (MobileReaderInfo m : this) {
				if (m.getUUID().equals(o)) {
					return true;
				}
			}
		}

		return this._helper.contains(o);

	}

	@Override
	public synchronized Iterator<MobileReaderInfo> iterator() {
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
	protected synchronized boolean addWithoutFireEvent(MobileReaderInfo e) {

		return this._helper.add(e);
	}

	@Override
	protected synchronized boolean removeWithoutFireEvent(Object o) {

		return this._helper.remove(o);
	}

	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		return this._helper.containsAll(c);
	}

}
