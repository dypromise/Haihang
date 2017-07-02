/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.flightproxy;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.Configuration;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.flightproxy.util.DirUtils;

public class FlightProxyConfiguration {
	
	protected static Logger _logger = Logger.getLogger(FlightProxyConfiguration.class);
	
	public static final int BSM_LAYOUT_FLAT = 0;
	public static final int BSM_LAYOUT_STRUCTURED = 1;
	public static final String BSM_LAYOUT_FLAT_STRING = "flat";
	public static final String BSM_LAYOUT_STRUCTURED_STRING = "structured";
	
	public static final String DEFAULT_BSM_ROOT_DIR = "/Users/dingyang/Public/Haihang/runable2/BSM";
	public static final String DEFAULT_BSM_BACKUP_DIR = "/Users/dingyang/Public/Haihang/runable2/BSM/backup";
	
	private static String _uuid = null;
	private static String _name = null;
	private static String _bsmRootDir = null;
	private static int _bsmLayout = -1;
	private static String _bsmBackupDir = null;
	private static HashSet<String> _allowedFlights = null;
	
	public static String uuid() {
		if (null == _uuid) {
			_uuid = Configuration.getString("flightProxy[@uuid]");
		}
		
		return _uuid;
	}
	
	public static String name() {
		if (null == _name) {
			_name = Configuration.getString("flightProxy[@name]");
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
//			Configuration.saveProperty("flightProxy[@name]", name);
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
	
	public static String bsmRootDir() {
		if (null == _bsmRootDir) {
			try {
				_bsmRootDir = Configuration.getString("flightProxy.dcsAdapter[@bsmRootDir]");
				DirUtils.validateDir(_bsmRootDir);
			} catch (Exception e) {
				_logger.warn(String.format(
						"Failed to validate the configured BSM root directory. Default directory (%s) will be used.",
						DEFAULT_BSM_ROOT_DIR));
				
				_bsmRootDir = DEFAULT_BSM_ROOT_DIR;
				
				try {
					DirUtils.validateDir(_bsmRootDir);
				} catch (Exception e2) {
					_logger.fatal("Failed to validate the default BSM root directory. The system will exit.");
					System.exit(1);
				}
			}
		}
		
		return _bsmRootDir;
	}
	
	public static int bsmLayout() {
		if (_bsmLayout < 0) {
			try {
				String bsmlayout = Configuration.getString("flightProxy.dcsAdapter[@bsmLayout]");
				if (bsmlayout.equals(BSM_LAYOUT_FLAT_STRING)) {
					_bsmLayout = BSM_LAYOUT_FLAT;
				} else if (bsmlayout.equals(BSM_LAYOUT_STRUCTURED_STRING)) {
					_bsmLayout = BSM_LAYOUT_STRUCTURED;
				} else {
					_logger.warn(String.format(
							"BSM layout unsupported: %s. Default layout (%s) will be used.",
							bsmlayout, BSM_LAYOUT_FLAT_STRING));
					
					_bsmLayout = BSM_LAYOUT_FLAT;
				}
			} catch (Exception e) {
				_logger.warn(String.format(
						"BSM layout unspecified. Default layout (%s) will be used.",
						BSM_LAYOUT_FLAT_STRING));
				
				_bsmLayout = BSM_LAYOUT_FLAT;
			}
		}
		
		return _bsmLayout;
	}
	
	public static String bsmBackupDir() {
		if (null == _bsmBackupDir) {
			try {
				_bsmBackupDir = Configuration.getString("flightProxy.dcsAdapter[@bsmBackupDir]");
				DirUtils.validateDir(_bsmBackupDir);
			} catch (Exception e) {
				_logger.warn(String.format(
						"Failed to validate the configured BSM backup directory. Default directory (%s) will be used.",
						DEFAULT_BSM_BACKUP_DIR));
				
				_bsmBackupDir = DEFAULT_BSM_BACKUP_DIR;
				
				try {
					DirUtils.validateDir(_bsmBackupDir);
				} catch (Exception e2) {
					_logger.fatal("Failed to validate the default BSM backup directory. The system will exit.");
					System.exit(1);
				}
			}
		}
		
		return _bsmBackupDir;
	}
	
	public static HashSet<String> allowedFlights() {
		if (null == _allowedFlights) {
			_allowedFlights = new HashSet<String>();
			try {
				String allowed = Configuration.getString("flightProxy.flightsFilter[@allowed]");
				_allowedFlights.addAll(Arrays.asList(allowed.split(";")));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return _allowedFlights;
	}
	
}