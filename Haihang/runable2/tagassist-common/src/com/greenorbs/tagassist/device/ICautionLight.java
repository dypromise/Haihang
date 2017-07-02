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
 * This interface is the abstraction of light It contains methods corresponding
 * to the light's function
 * 
 * @author Haoxiang
 * 
 */
public interface ICautionLight extends IHardware{

	public static final int BLACK = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int YELLOW = 4;
	public static final int PURPLE = 5;
	public static final int CYAN = 6;
	public static final int WHITE = 7;

	void turnOn(int light) throws HardwareException;

	void turnOff(int light) throws HardwareException;

}