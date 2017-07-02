/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, May 26, 2012
 */

package com.greenorbs.tagassist.flightproxy.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.greenorbs.tagassist.flightproxy.FlightProxyConfiguration;

public class DirUtils {
	
	protected static Logger _logger = Logger.getLogger(DirUtils.class);
	
	private static final SimpleDateFormat BACKUP_DIR_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static int _lastBackupDay = -1;
	private static File _lastBackupDir = null;

	public static void validateDir(String path) {
		File f = new File(path);
		if (!(f.exists() && f.isDirectory())) {
			f.mkdirs();
		}
	}
	
	public static File getNextFile(File dir) {
		if (null == dir) {
			return null;
		}
		
		try {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					if (f.isFile() && f.getName().endsWith(".txt")) {
						return f;
					}
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			_logger.error(e);
		}

		return null;
	 	
//		Collection<File> files = FileUtils.listFiles(dir, new SuffixFileFilter(".txt"), null);
//		if (!files.isEmpty()) {
//			return files.iterator().next();
//		} else {
//			return null;
//		}
	}
	
	public static void backupFile(File file) {
		File backupDir = DirUtils.getBackupDir();
		try {
			FileUtils.moveFileToDirectory(file, backupDir, true);
		} catch (IOException e) {
			_logger.warn(e);
			try {
				FileUtils.copyFileToDirectory(file, backupDir, true);
				FileUtils.forceDelete(file);
			} catch (IOException e2) {
				_logger.error(e2);
			}
		}
	}
	
	public static File getBackupDir() {
		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		if (currentDay != _lastBackupDay) {
			_lastBackupDay = currentDay;
			_lastBackupDir = new File(String.format("%s/%s", 
					FlightProxyConfiguration.bsmBackupDir(),
					BACKUP_DIR_FORMAT.format(new Date())));
		}
		
		return _lastBackupDir;
	}
	
}
