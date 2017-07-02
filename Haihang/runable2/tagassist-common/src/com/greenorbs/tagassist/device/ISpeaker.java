/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Haoxiang Liu, 2012-2-8
 */
package com.greenorbs.tagassist.device;

import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;

/**
 * This interface is the abstraction of speaker
 * It contains methods corresponding to the speaker's function
 * @author Haoxiang
 *
 */

public interface ISpeaker extends IHardware{
	
	public static final int LONG_MODE = 1;
	
	public static final int SHORT_MODE = 0;
	
	public static final int MIDDLE_MODE = 2;
	
	// speak------------------------------------
	
	/**
	 * Speaker speaks out
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	void speak(int mode) throws HardwareException;
	
	
	void mute()throws HardwareException;
}
