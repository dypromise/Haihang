/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-4
 */

package com.greenorbs.tagassist;

import java.beans.PropertyChangeListener;

public interface IPropertySupport {
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	public PropertyChangeListener[] getPropertyChangeListeners();

	public void addPropertyChangeListener(String property, PropertyChangeListener listener);

	public void removePropertyChangeListener(String property, PropertyChangeListener listener);
	
	public PropertyChangeListener[] getPropertyChangeListeners(String property);
	
	public Object getProperty(String property);
	
	public void setProperty(String property, Object value);

}
