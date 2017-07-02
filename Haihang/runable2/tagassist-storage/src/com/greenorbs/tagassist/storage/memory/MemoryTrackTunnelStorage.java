/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-25
 */

package com.greenorbs.tagassist.storage.memory;

import java.util.Collection;
import java.util.Iterator;

import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.storage.TrackTunnelStorage;

public class MemoryTrackTunnelStorage extends TrackTunnelStorage {

	private MemoryStorageHelper<TrackTunnelInfo> _helper;

	public MemoryTrackTunnelStorage() {

		this._helper = new MemoryStorageHelper<TrackTunnelInfo>(this);
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
			for (TrackTunnelInfo t : this) {
				if (t.getUUID().equals(o)) {
					return true;
				}
			}
		}
		return this._helper.contains(o);
	}

	@Override
	public synchronized Iterator<TrackTunnelInfo> iterator() {
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
	protected synchronized boolean addWithoutFireEvent(TrackTunnelInfo e) {
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

	@Override
	public synchronized TrackTunnelInfo findByTrackTunnelId(String trackTunnelId) {
		Iterator<TrackTunnelInfo> it = this.iterator();

		while (it.hasNext()) {
			TrackTunnelInfo track = it.next();
			if (track.getUUID().equals(trackTunnelId)) {

				return track;
			}
		}

		return null;
	}
}
