/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-12
 */

package com.greenorbs.tagassist.device;

public interface IPeripheral extends InfraredRay, ISpeaker, ICautionLight{

	public void setIP(String ip);
	
	public String getIP();
	
	public void setLocalPort(int port);
	
	public int getLocalPort();
	
	public void setTargetPort(int port);
	
	public int getTargetPort();
	
	public void setDetectionInterval(long interval);
	
	public long getDetectionInterval();
}
