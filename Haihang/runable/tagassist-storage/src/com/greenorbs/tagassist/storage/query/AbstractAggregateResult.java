/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-4
 */

package com.greenorbs.tagassist.storage.query;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import com.greenorbs.tagassist.IPropertySupport;

public abstract class AbstractAggregateResult<E extends IPropertySupport>
		implements IAggregateResult<E>, IPropertySupport {

	protected Map<String, Object> _properties = new HashMap<String, Object>();

	protected PropertyChangeSupport _listeners = new PropertyChangeSupport(this);

	public AbstractAggregateResult() {

	}

	public AbstractAggregateResult(Object value) {

		this.setValue(value);
	}

	@Override
	public Object getValue() {
		return this._properties.get(PROPERTY_VALUE);
	}

	@Override
	public void setValue(Object value) {
		Object oldValue = this.getValue();
		this._properties.put(PROPERTY_VALUE, value);
		this._listeners.firePropertyChange(PROPERTY_VALUE, oldValue, value);
	}

	@Override
	public void addPropertyChangeListener(String property,
			PropertyChangeListener listener) {
		this._listeners.addPropertyChangeListener(property, listener);
	}

	@Override
	public void removePropertyChangeListener(String property,
			PropertyChangeListener listener) {
		this._listeners.removePropertyChangeListener(property, listener);
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners(String property) {
		return this._listeners.getPropertyChangeListeners(property);
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return this._listeners.getPropertyChangeListeners();
	}

	@Override
	public Object getProperty(String property) {
		return this._properties.get(property);
	}

	@Override
	public void setProperty(String property, Object value) {
		this._properties.put(property, value);

	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.addPropertyChangeListener(PROPERTY_VALUE, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.removePropertyChangeListener(PROPERTY_VALUE, listener);
	}
}
