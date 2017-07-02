/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 13, 2012
 */

package com.greenorbs.tagassist.flightproxy.dcsadapter;

public interface IDCSMessageConsumer {
	
	public void onDCSMessage(DCSMessage message);
	
}
