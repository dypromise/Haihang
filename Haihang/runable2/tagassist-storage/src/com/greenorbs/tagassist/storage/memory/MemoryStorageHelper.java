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

import java.util.Vector;

import com.greenorbs.tagassist.IPropertySupport;
import com.greenorbs.tagassist.storage.AbstractStorage;

class MemoryStorageHelper<E extends IPropertySupport> extends Vector<E> {

	private static final long serialVersionUID = 1L;

	AbstractStorage<E> _storage;

	MemoryStorageHelper(AbstractStorage<E> storage) {

		this._storage = storage;
	}


}
