/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-10
 */

package com.greenorbs.tagassist.device;

import java.util.EventObject;

public class InfraredRayEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private String _rayId;

	private long _timestamp;

	public InfraredRayEvent(Object source, String rayId, long timestamp) {
		super(source);
		this.setRayId(rayId);
		this.setTimestamp(timestamp);
	}

	public String getRayId() {
		return _rayId;
	}

	public void setRayId(String rayId) {
		_rayId = rayId;
	}

	public long getTimestamp() {
		return _timestamp;
	}

	public void setTimestamp(long timestamp) {
		_timestamp = timestamp;
	}

}
