/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-6
 */

package com.greenorbs.tagassist.device.simulation;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.ICautionLight;

public class SimulatedCautionLight implements ICautionLight {

	@Override
	public void startup() throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Result shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result reset() throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void turnOn(int lights) throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOff(int lights) throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	
	
}
