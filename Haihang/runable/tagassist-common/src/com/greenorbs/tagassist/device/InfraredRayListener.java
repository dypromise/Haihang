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
 * This interface defines a listener that listens to a moving object that passes
 * through the check tunnel. When the action is caught, the reader is triggered
 * to read the tags.
 * 
 * @author Lei Yang
 * 
 */
public interface InfraredRayListener {

	public void objectDetected(InfraredRayDetectEvent event);
	
}
