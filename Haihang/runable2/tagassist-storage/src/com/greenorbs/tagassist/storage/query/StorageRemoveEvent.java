/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-3
 */

package com.greenorbs.tagassist.storage.query;

public class StorageRemoveEvent<E> extends StorageEvent{

	private static final long serialVersionUID = 1L;
	
	private E _item;
	
	public StorageRemoveEvent(Object source, Object trigger, E item) {
		super(source, trigger);
		
		this.setItem(item);
	}

	public E getItem() {
		return _item;
	}

	public void setItem(E item) {
		_item = item;
	}

	
}