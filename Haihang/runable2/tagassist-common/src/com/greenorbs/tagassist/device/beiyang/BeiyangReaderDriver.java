/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-27
 */

package com.greenorbs.tagassist.device.beiyang;

import com.sun.jna.Library;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

public interface BeiyangReaderDriver extends Library{

	public int BYRD_InitPort(int gPort, String pIp);
	
	public int BYRD_ClosePort(int hComm);
	
	public int BYRD_SetAntennaPower(int hComm, int nIndex, String pValue);
	
	public int BYRD_GetAntennaPower(int hComm, int nIndex, byte [] pValueParam);
	
	public int BYRD_SetInventTag6c(int hComm, int nIndex);
	
	public int BYRD_GetInventTag6c(int hComm, ByteByReference pInfo, IntByReference nCount, IntByReference nFinish);
	
	public int BYRD_GetInventTag6cWithRSSI(int hComm, ByteByReference pInfo, IntByReference nCount, IntByReference nFinish);
	
	public int BYRD_SetTagType(int hComm, int nIndex, byte [] pValue);
	
	public int BYRD_SetWorkMode(int hComm, int nIndex, int nMode);
	
}
