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

import java.util.EventObject;

public class StorageEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private Object _trigger;
	
	public StorageEvent(Object source, Object trigger) {
		super(source);
		this._trigger = trigger;
	}

	public Object getTrigger() {
		return _trigger;
	}

	public void setTrigger(Object trigger) {
		_trigger = trigger;
	}

	

}
