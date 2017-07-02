/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.datacenter;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.Configuration;
import com.greenorbs.tagassist.Result;

public class DataCenterConfiguration {
	
	protected static Logger _logger = Logger.getLogger(DataCenterConfiguration.class);
	
	private static String _uuid = null;
	private static String _name = null;
	
	public static String uuid() {
		if (null == _uuid) {
			_uuid = Configuration.getString("dataCenter[@uuid]");
		}
		
		return _uuid;
	}
	
	public static String name() {
		if (null == _name) {
			_name = Configuration.getString("dataCenter[@name]");
		}
		
		return _name;
	}
	
	public static Result name(String name) {
		Result result = new Result(Result.CODE_FAILURE);
		
		/*
		 *  FIXME Known bug: the use of Configuration.saveProperty()
		 *  would erase all the contents of the configuration file.
		 */
//		try {
//			Configuration.saveProperty("dataCenter[@name]", name);
//			result.setCode(Result.CODE_SUCCESS);
//		} catch (ConfigurationException e) {
//			result.setCode(Result.CODE_ERROR);
//			result.setDescription(e.getMessage());
//			
//			e.printStackTrace();
//			_logger.error(e.getMessage());
//		}
		
		return result;
	}
	
}