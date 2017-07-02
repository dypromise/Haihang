/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-3
 */

package com.greenorbs.tagassist.device.beiyang;

import com.sun.jna.Library;

public interface BeiyangPeripheralDriver extends Library{
	
	public int BYRD_initPort(int lPort, int nPort, String pIp);
	
	public int BYRD_closePort(int h);
	
	public int BYRD_recInfo(int h, byte [] data, int len);
	
	public int BYRD_sendInfo(int h, byte[] data, int len);
	
}
