/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-3
 */

package com.greenorbs.tagassist.device;


public class InfraredRayDetectEvent extends InfraredRayEvent {

	private static final long serialVersionUID = 1L;

	public InfraredRayDetectEvent(Object source, String rayId, long time) {
		super(source, rayId, time);
	}

	
	@Override
	public String toString(){
		return "[RayId:"+this.getRayId()+", timestamp:"+this.getTimestamp()+"]";
	}

}
