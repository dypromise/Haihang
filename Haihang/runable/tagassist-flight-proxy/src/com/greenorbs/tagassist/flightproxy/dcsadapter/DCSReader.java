/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 25, 2012
 */

package com.greenorbs.tagassist.flightproxy.dcsadapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.flightproxy.FlightProxyConfiguration;
import com.greenorbs.tagassist.flightproxy.util.DirUtils;

public class DCSReader {
	
	protected static Logger _logger = Logger.getLogger(DCSReader.class);
	
	private static final SimpleDateFormat STRUCTURED_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	private int _bsmLayout;
	private File _lastWorkingDir;
	private File _workingDir;
	private int _workingDay;
	private HashSet<String> _magicPool;
	private Random _random;
	
	public DCSReader() {
		_bsmLayout = FlightProxyConfiguration.bsmLayout();
		if (_bsmLayout == FlightProxyConfiguration.BSM_LAYOUT_STRUCTURED) {
			_lastWorkingDir = null;
			_workingDir = new File(String.format("%s/%s", 
					FlightProxyConfiguration.bsmRootDir(),
					STRUCTURED_FORMAT.format(new Date())));
			_workingDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		} else {
			_workingDir = new File(FlightProxyConfiguration.bsmRootDir());
		}
		_magicPool = new HashSet<String>();
		_random = new Random((new Date()).getTime());
	}
	
	public ArrayList<DCSBlock> read() {
		if (_bsmLayout == FlightProxyConfiguration.BSM_LAYOUT_STRUCTURED) {
			int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
			if (currentDay != _workingDay) {
				if (DirUtils.getNextFile(_workingDir) == null) {
					_lastWorkingDir = _workingDir;
					_workingDir = new File(String.format("%s/%s", 
							FlightProxyConfiguration.bsmRootDir(),
							STRUCTURED_FORMAT.format(new Date())));
					_workingDay = currentDay;
				}
			} else {
				// FIXME workaround
				if (DirUtils.getNextFile(_workingDir) == null) {
					if (_random.nextBoolean() && _lastWorkingDir != null) {
						return this.read(_lastWorkingDir);
					}
				}
			}
		}
		return this.read(_workingDir);
	}
	
	private ArrayList<DCSBlock> read(File dir) {
		File f = DirUtils.getNextFile(dir);
		
		if (null == f) {
			return null;
		}
		
		ArrayList<DCSBlock> blocks = DCSBlockReader.readBlocks(f);
		if (blocks != null) {
			if (blocks.isEmpty() && !_magicPool.contains(f.getName())) {
				_magicPool.add(f.getName());
				_logger.info("File added into magic pool: " + f.getName());
			} else {
				DirUtils.backupFile(f);
				_logger.info("File backed up: " + f.getName());
				
				if (_magicPool.contains(f.getName())) {
					_magicPool.remove(f.getName());
					_logger.info("File removed from magic pool: " + f.getName());
				}
			}
		}
		
		return blocks;
	}
	
}
