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

import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.storage.CheckTunnelStorage;
import com.greenorbs.tagassist.storage.query.CountResult;
import com.greenorbs.tagassist.storage.query.IQuery;

public class MemoryCheckTunnelStorage extends CheckTunnelStorage {

	private MemoryStorageHelper<CheckTunnelInfo> _helper;

	public MemoryCheckTunnelStorage() {

		_helper = new MemoryStorageHelper<CheckTunnelInfo>(this);

	}

	/******* Collection interface **************/

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
			for (CheckTunnelInfo c : this) {
				if (c.getUUID().equals(o)) {
					return true;
				}
			}
		}
		return this._helper.contains(o);
	}

	@Override
	public synchronized Iterator<CheckTunnelInfo> iterator() {
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
	protected synchronized boolean addWithoutFireEvent(CheckTunnelInfo e) {
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

	/*************************************/
	// @Override
	// public boolean contains(String checkTunnelId) {
	// return findByCheckTunnelId(checkTunnelId) != null;
	// }

	@Override
	public synchronized CheckTunnelInfo findByCheckTunnelId(String checkTunnelId) {
		Iterator<CheckTunnelInfo> it = this._helper.iterator();

		while (it.hasNext()) {
			CheckTunnelInfo c = it.next();
			if (c.getUUID() != null) {
				if (c.getUUID().equals(checkTunnelId)) {
					return c;
				}
			}
		}

		return null;
	}

	public synchronized CheckTunnelInfo findByFlightId(String flightId) {

		Iterator<CheckTunnelInfo> it = this._helper.iterator();
		while (it.hasNext()) {
			CheckTunnelInfo c = it.next();
			if (c.getBoundFlightId() != null) {
				if (c.getBoundFlightId().equals(flightId)) {

					return c;

				}
			}
		}

		return null;
	}

}
