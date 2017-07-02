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

/**
 * This interface defines an abstraction of the underlying infrared ray
 * interface. It defines a set of methods. A class that implements the interface
 * agrees to implement all the methods defined in the interface, thereby
 * agreeing to certain behavior.
 * 
 * @author Haoxiang
 * 
 */

public interface InfraredRay extends IHardware{

	public void addDetectionListener(InfraredRayListener listener);

	public void removeDetectionListener(InfraredRayListener listener);

	public String[] getInfraredPoints();

}
