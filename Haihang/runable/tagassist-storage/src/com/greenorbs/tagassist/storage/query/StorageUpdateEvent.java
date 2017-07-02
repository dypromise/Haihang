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

public class StorageUpdateEvent<E> extends StorageEvent {

	private static final long serialVersionUID = 1L;

	private String _propertyName;

	private E _item;

	private Object _oldValue;

	private Object _newValue;

	public StorageUpdateEvent(Object source, Object trigger, E item, String propertyName,
			Object oldValue, Object newValue) {
		super(source, trigger);
		this.setPropertyName(propertyName);
		this.setOldValue(oldValue);
		this.setNewValue(newValue);
		this.setItem(item);
	}

	public String getPropertyName() {
		return _propertyName;
	}

	public void setPropertyName(String propertyName) {
		_propertyName = propertyName;
	}

	public Object getOldValue() {
		return _oldValue;
	}

	public void setOldValue(Object oldValue) {
		_oldValue = oldValue;
	}

	public Object getNewValue() {
		return _newValue;
	}

	public void setNewValue(Object newValue) {
		_newValue = newValue;
	}

	public E getItem() {
		return _item;
	}

	public void setItem(E item) {
		_item = item;
	}

	@Override
	public String toString() {
		return "[property:" + this._propertyName + ", oldValue:" + this._oldValue + ", newValue:"
				+ this._newValue + "]";
	}

}
