/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-9
 */

package com.greenorbs.tagassist.device;

public class InfraredRayChangeEvent extends InfraredRayEvent {

	private static final long serialVersionUID = 1L;

	public static final int STATUS_DETECT = 1;

	public static final int STATUS_LOST = 2;

	private int _status;

	public InfraredRayChangeEvent(Object source, String rayId, long timestamp, int status) {
		super(source, rayId, timestamp);

		this.setStatus(status);
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

}
