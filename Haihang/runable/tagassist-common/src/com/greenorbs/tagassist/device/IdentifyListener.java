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
 * This interface should be implemented in order to be able to receive tag
 * identification observations asynchronously.
 * @author Haoxiang
 *
 */

public interface IdentifyListener {
	/**
	 * This callback method is called whenever an asynchronous identify has been performed.
	 * 
	 * @param observation
	 */
	void identifyPerformed(ObservationReport report);
}
