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

import com.greenorbs.tagassist.WristbandInfo;
import com.greenorbs.tagassist.storage.WristbandStorage;

//
public class MemoryWristbandStorage extends WristbandStorage {

	private MemoryStorageHelper<WristbandInfo> _helper;

	public MemoryWristbandStorage() {
		this._helper = new MemoryStorageHelper<WristbandInfo>(this);
	}

	@Override
	protected synchronized boolean addWithoutFireEvent(WristbandInfo item) {
		return this._helper.add(item);
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
			for (WristbandInfo w : this) {
				if (w.getUUID().equals(o)) {
					return true;
				}
			}
		}
		return this._helper.contains(o);
	}

	@Override
	public synchronized Iterator<WristbandInfo> iterator() {
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
	public synchronized boolean containsAll(Collection<?> c) {
		return this._helper.containsAll(c);
	}

	@Override
	public synchronized WristbandInfo findByWristbandId(String wristbandId) {

		Iterator<WristbandInfo> it = this._helper.iterator();
		while (it.hasNext()) {
			WristbandInfo w = it.next();
			if (w.getUUID().equals(wristbandId)) {

				return w;

			}
		}

		return null;
	}

	@Override
	protected synchronized boolean removeWithoutFireEvent(Object o) {
		return this._helper.remove(o);
	}

}
