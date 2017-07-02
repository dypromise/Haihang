/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-22
 */

package com.greenorbs.tagassist.storage.query;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.greenorbs.tagassist.Configuration;
import com.greenorbs.tagassist.storage.BaggageStorage;
import com.greenorbs.tagassist.storage.CarriageStorage;
import com.greenorbs.tagassist.storage.CheckTunnelStorage;
import com.greenorbs.tagassist.storage.FlightStorage;
import com.greenorbs.tagassist.storage.IStorage;
import com.greenorbs.tagassist.storage.MobileReaderStorage;
import com.greenorbs.tagassist.storage.NotificationStorage;
import com.greenorbs.tagassist.storage.TrackTunnelStorage;
import com.greenorbs.tagassist.storage.WristbandStorage;
import com.greenorbs.tagassist.util.ObjectUtils;

public class StorageFactory implements ApplicationContextAware {

	private static Logger _log = Logger.getLogger(StorageFactory.class);

	final static String BAGGAGE_STORAGE_NAME = "baggageStorage";
	final static String FLIGHT_STORAGE_NAME = "flightStorage";
	final static String CHECK_TUNNEL_STORAGE_NAME = "checkTunnelStorage";
	final static String NOTIFICATION_STORAGE_NAME = "notificationStorage";
	final static String WRISTBAND_STORAGE_NAME = "wristbandStorage";
	final static String TRACK_TUNNEL_STORAGE_NAME = "trackTunnelStorage";
	final static String MOBILE_READER_STORAGE_NAME = "mobileReaderStorage";

	static BaggageStorage _baggageStorage;
	static FlightStorage _flightStorage;
	static CheckTunnelStorage _checkTunnelStorage;
	static NotificationStorage _notificationStorage;
	static WristbandStorage _wristbandStorage;
	static TrackTunnelStorage _trackTunnelStorage;
	static MobileReaderStorage _mobileReaderStorage;

	static ApplicationContext _context;

	private static IStorage<?> loadStorageFromSpring(String name) {
		
		if (_context == null) {
			return null;
		}

		IStorage<?> storage = (IStorage<?>) _context.getBean(name);

		if (storage == null) {
			_log.warn("unable to load the storage from spring configuration ["
					+ name
					+ "]. It will be loaded from the norma configuration.");
		} else {
			storage.initialize();
			_log.info("Successfullyload the storage from spring configuration ["
					+ name + "]");
		}

		return storage;

	}

	private static IStorage<?> loadStorageFromConfiguration(String name) {
		try {
			int size = Configuration.getSize("storages.storage");
			IStorage<?> storage = null;
			for (int i = 0; i < size; i++) {
				String n = Configuration.getString("storages.storage(" + i
						+ ")[@name]");
				if (n.equals(name)) {
					String cls = Configuration.getString("storages.storage("
							+ i + ")[@class]");
					storage = (IStorage<?>) ObjectUtils.newInstance(cls);
					break;
				}
			}

			if (storage == null) {
				_log.info("It fails to load storage from normal configuration["
						+ name + "]");
			} else {
				storage.initialize();
				_log.info("It is successful to load storage from normal configuration["
						+ name + "]");

			}

			return storage;

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("It fails to load storage:" + name);
		}

		return null;

	}

	private static IStorage<?> loadStorage(String name) {

		IStorage<?> storage = loadStorageFromSpring(name);
		if (storage == null) {
			storage = loadStorageFromConfiguration(name);
		}

		return storage;

	}

	/**
	 * Get the baggageStoage isntance Revised by Lei Yang, 2013/1/17. Change the
	 * static method to be synchronized.
	 * 
	 * @return
	 */
	public static synchronized BaggageStorage getBaggageStorage() {

		if (_baggageStorage == null) {
			_baggageStorage = (BaggageStorage) loadStorage(BAGGAGE_STORAGE_NAME);
		}

		return _baggageStorage;
	}

	/**
	 * Revised by Lei Yang, 2013/1/17. Change the static method to be
	 * synchronized.
	 * 
	 * @return
	 */
	public static synchronized FlightStorage getFlightStorage() {

		if (_flightStorage == null) {
			_flightStorage = (FlightStorage) loadStorage(FLIGHT_STORAGE_NAME);
		}

		return _flightStorage;
	}

	/**
	 * Revised by Lei Yang, 2013/1/17. Change the static method to be
	 * synchronized.
	 * 
	 * @return
	 */
	public static synchronized CheckTunnelStorage getCheckTunnelStorage() {
		if (_checkTunnelStorage == null) {
			_checkTunnelStorage = (CheckTunnelStorage) loadStorage(CHECK_TUNNEL_STORAGE_NAME);
		}

		return _checkTunnelStorage;
	}

	/**
	 * Revised by Lei Yang, 2013/1/17. Change the static method to be
	 * synchronized.
	 * 
	 * @return
	 */
	public static synchronized NotificationStorage getNotificationStorage() {
		if (_notificationStorage == null) {
			_notificationStorage = (NotificationStorage) loadStorage(NOTIFICATION_STORAGE_NAME);
		}

		return _notificationStorage;
	}

	/**
	 * Revised by Lei Yang, 2013/1/17. Change the static method to be
	 * synchronized.
	 * 
	 * @return
	 */
	public static synchronized WristbandStorage getWristbandStorage() {
		if (_wristbandStorage == null) {
			_wristbandStorage = (WristbandStorage) loadStorage(WRISTBAND_STORAGE_NAME);
		}
		return _wristbandStorage;
	}

	/**
	 * Revised by Lei Yang, 2013/1/17. Change the static method to be
	 * synchronized.
	 * 
	 * @return
	 */
	public static synchronized TrackTunnelStorage getTrackTunnelStorage() {
		if (_trackTunnelStorage == null) {
			_trackTunnelStorage = (TrackTunnelStorage) loadStorage(TRACK_TUNNEL_STORAGE_NAME);
		}
		return _trackTunnelStorage;
	}

	/**
	 * Revised by Lei Yang, 2013/1/17. Change the static method to be
	 * synchronized.
	 * 
	 * @return
	 */
	public static synchronized MobileReaderStorage getMobileReaderStorage() {
		if (_mobileReaderStorage == null) {
			_mobileReaderStorage = (MobileReaderStorage) loadStorage(MOBILE_READER_STORAGE_NAME);
		}

		return _mobileReaderStorage;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		_context = context;

	}

	public static CarriageStorage getCarriageStorage() {
		// TODO Auto-generated method stub
		return null;
	}

}
