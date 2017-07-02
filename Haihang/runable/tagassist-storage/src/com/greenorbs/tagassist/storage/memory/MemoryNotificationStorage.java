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

import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.storage.NotificationStorage;

public class MemoryNotificationStorage extends NotificationStorage {

	private MemoryStorageHelper<NotificationInfo> _helper;

	public MemoryNotificationStorage() {

		this._helper = new MemoryStorageHelper<NotificationInfo>(this);

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
			for (NotificationInfo n : this) {
				if (n.getUUID().equals(o)) {
					return true;
				}
			}
		}

		return this._helper.contains(o);
	}

	@Override
	public synchronized Iterator<NotificationInfo> iterator() {
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
	protected synchronized boolean addWithoutFireEvent(NotificationInfo e) {
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

	/* ==========NotificationStorage============= */
	//
	// @Override
	// public boolean contains(String notificationUUID) {
	// return find(notificationUUID) == null ? false : true;
	// }

	@Override
	public synchronized NotificationInfo findByNotificationId(
			String notificatinUUID) {

		Iterator<NotificationInfo> it = this.iterator();
		while (it.hasNext()) {
			NotificationInfo n = it.next();
			if (n.getUUID().equals(notificatinUUID)) {

				return n;
			}
		}

		return null;
	}

}
