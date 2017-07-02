/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Haoxiang Liu, 2012-3-6
 */


package com.greenorbs.tagassist.device;


public interface DetectChangedListener {
	
	public void detectStatusChanged(int id) throws HardwareException;

}
