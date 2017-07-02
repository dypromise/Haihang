/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Tianci Liu, 2012-3-27
 */

package com.greenorbs.tagassist;

public class BaggageTrackedProperty {
	
	private float _distance;
	
	private long _timeStamp;

	public float getDistance() {
		return _distance;
	}

	public void setDistance(float distance) {
		this._distance = distance;
	}

	public long getTimeStamp() {
		return _timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this._timeStamp = timeStamp;
	}
	
}
