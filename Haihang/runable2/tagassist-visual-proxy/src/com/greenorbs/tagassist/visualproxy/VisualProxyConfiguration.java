/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.visualproxy;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.Configuration;
import com.greenorbs.tagassist.Result;

public class VisualProxyConfiguration {
	
	protected static Logger _logger = Logger.getLogger(VisualProxyConfiguration.class);
	
	private static String _uuid = null;
	private static String _name = null;
	
	public static String uuid() {
		if (null == _uuid) {
			_uuid = Configuration.getString("visualProxy[@uuid]");
		}
		
		return _uuid;
	}
	
	public static String name() {
		if (null == _name) {
			_name = Configuration.getString("visualProxy[@name]");
		}
		
		return _name;
	}
	
	public static Result name(String name) {
		return Result.FAILURE;
	}
	
}