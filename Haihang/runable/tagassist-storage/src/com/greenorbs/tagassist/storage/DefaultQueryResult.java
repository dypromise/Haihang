/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-28
 */

package com.greenorbs.tagassist.storage;


import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.greenorbs.tagassist.IPropertySupport;
import com.greenorbs.tagassist.storage.query.IQueryResult;
import com.greenorbs.tagassist.storage.query.StorageAddEvent;
import com.greenorbs.tagassist.storage.query.StorageRemoveEvent;
import com.greenorbs.tagassist.storage.query.StorageUpdateEvent;

public class DefaultQueryResult<E extends IPropertySupport> implements
		IQueryResult<E> {

	protected List<E> _result = new Vector<E>();

	@Override
	public void itemAdded(StorageAddEvent<E> event) {
		this._result.add(event.getItem());
	}

	@Override
	public void itemRemoved(StorageRemoveEvent<E> event) {
		this._result.remove(event.getItem());
	}

	@Override
	public void itemUpdated(StorageUpdateEvent<E> event) {

		// keep nothing

	}

	public int size() {
		return this._result.size();
	}

	public Iterator<E> iterator() {
		return this._result.iterator();
	}

	@Override
	public boolean contains(E item) {
		return _result.contains(item);
	}

	public E get(int index) {
		return this._result.get(index);
	}
}
