/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-5
 */

package com.greenorbs.tagassist;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

public abstract class AbstractPropertySupport implements IPropertySupport,
		Cloneable, Serializable {

	private static final long serialVersionUID = -6290557446222964609L;

	protected PropertyChangeSupport _propertyChangeListeners = new PropertyChangeSupport(
			this);

	protected HashMap<String, Object> _properties = new HashMap<String, Object>();
	
	protected PropertyChangeListener _listenerAll = null;

	public void addPropertyChangeListener(String property,
			PropertyChangeListener listener) {
		_propertyChangeListeners.addPropertyChangeListener(property, listener);
	}

	public void removePropertyChangeListener(String property,
			PropertyChangeListener listener) {
		_propertyChangeListeners.addPropertyChangeListener(property, listener);
	}

	@Transient
	@JsonIgnoreField
	public PropertyChangeListener[] getPropertyChangeListeners(String property) {
		return this._propertyChangeListeners
				.getPropertyChangeListeners(property);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {

		this._propertyChangeListeners.addPropertyChangeListener(listener);
		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this._propertyChangeListeners.removePropertyChangeListener(listener);
	}

	@Transient
	@JsonIgnoreField
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return this._propertyChangeListeners.getPropertyChangeListeners();
	}

	protected void firePropertyChange(String prop, Object oldValue,
			Object newValue) {
		_propertyChangeListeners.firePropertyChange(prop, oldValue, newValue);
	}

	@Transient
	@JsonIgnoreField
	public Object getProperty(String propertyName) {
		return this._properties.get(propertyName);
	}

	@Transient
	@JsonIgnoreField
	public void setProperty(String propertyName, Object value) {
		Object oldValue = this._properties.get(propertyName);
		this._properties.put(propertyName, value);
		this.firePropertyChange(propertyName, oldValue, value);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		AbstractPropertySupport support = (AbstractPropertySupport) super
				.clone();

		support._properties = (HashMap<String, Object>) this._properties
				.clone();

		for (Map.Entry<String, Object> entry : this._properties.entrySet()) {
			support._properties.put(entry.getKey(), entry.getValue());
		}

		support._propertyChangeListeners = new PropertyChangeSupport(this);
		for (PropertyChangeListener l : this._propertyChangeListeners
				.getPropertyChangeListeners()) {
			support._propertyChangeListeners.addPropertyChangeListener(l);
		}

		return support;
	}
	
}
