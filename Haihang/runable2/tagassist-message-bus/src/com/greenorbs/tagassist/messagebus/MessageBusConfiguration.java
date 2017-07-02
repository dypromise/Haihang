/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.greenorbs.tagassist.Configuration;

public class MessageBusConfiguration implements ApplicationContextAware {

	protected static Logger _logger = Logger
			.getLogger(MessageBusConfiguration.class);

	public static final int DEFAULT_QUERY_TIMEOUT = 1000;
	public static final int DEFAULT_HEARTBEAT_INTERVAL = 30000;

	private String _brokerUrl = null;
	private int _defaultQueryTimeout = 0;
	private int _heartbeatInterval = 0;

	private static ApplicationContext _context;

	private static MessageBusConfiguration _instance;

	private static MessageBusConfiguration loadFromSpring() {
		if (_context == null) {
			return null;
		}

		try {
			MessageBusConfiguration config = (MessageBusConfiguration) _context
					.getBean("messageBusConfig");

			return config;
		} catch (Exception e) {
			return null;
		}
	}

	private static MessageBusConfiguration loadFromConfiguration() {
		MessageBusConfiguration config = new MessageBusConfiguration();

		// load brokerUrl
		config.setBrokerUrl(Configuration.getString("messageBus[@brokerUri]"));

		// load query timeout
		try {
			config.setDefaultQueryTimeout(Configuration
					.getInt("messageBus[@defaultQueryTimeout]"));
		} catch (Exception e) {
			config.setDefaultQueryTimeout(DEFAULT_QUERY_TIMEOUT);
			_logger.warn(String
					.format("Failed to read query timeout from configuration. Default timeout (%s) will be used.",
							DEFAULT_QUERY_TIMEOUT));
		}

		if (config.getDefaultQueryTimeout() <= 0) {
			config.setDefaultQueryTimeout(DEFAULT_QUERY_TIMEOUT);
			_logger.warn(String
					.format("Query timeout should be greater than 0. Default timeout (%s) will be used.",
							DEFAULT_QUERY_TIMEOUT));
		}

		// load heartbeat interval
		try {
			config.setHeartbeatInterval(Configuration
					.getInt("messageBus.heartbeat[@interval]"));
		} catch (Exception e) {
			config.setHeartbeatInterval(DEFAULT_HEARTBEAT_INTERVAL);
			_logger.warn(String
					.format("Failed to read heartbeat interval from configuration. Default interval (%s) will be used.",
							DEFAULT_HEARTBEAT_INTERVAL));
		}

		if (config.getHeartbeatInterval() <= 0) {
			config.setHeartbeatInterval(DEFAULT_HEARTBEAT_INTERVAL);
			_logger.warn(String
					.format("Heartbeat interval should be greater than 0. Default interval (%s) will be used.",
							DEFAULT_HEARTBEAT_INTERVAL));
		}

		return config;
	}

	private static MessageBusConfiguration instance() {
		if (_instance == null) {
			_instance = loadFromSpring();
		}

		if (_instance == null) {
			_logger.warn("Unable to load configuration from spring. Use config.xml instead.");
			_instance = loadFromConfiguration();
		}

		return _instance;
	}

	public static String brokerUri() {
		return instance().getBrokerUrl();
	}

	public static int defaultQueryTimeout() {
		return instance().getDefaultQueryTimeout();
	}

	public static int heartbeatInterval() {
		return instance().getHeartbeatInterval();
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		_context = context;
	}

	/**
	 * @return the defaultQueryTimeout
	 */
	public int getDefaultQueryTimeout() {
		return _defaultQueryTimeout;
	}

	/**
	 * @param defaultQueryTimeout
	 *            the defaultQueryTimeout to set
	 */
	public void setDefaultQueryTimeout(int defaultQueryTimeout) {
		_defaultQueryTimeout = defaultQueryTimeout;
	}

	/**
	 * @return the heartbeatInterval
	 */
	public int getHeartbeatInterval() {
		return _heartbeatInterval;
	}

	/**
	 * @param heartbeatInterval
	 *            the heartbeatInterval to set
	 */
	public void setHeartbeatInterval(int heartbeatInterval) {
		_heartbeatInterval = heartbeatInterval;
	}

	/**
	 * @return the brokerUrl
	 */
	public String getBrokerUrl() {
		return _brokerUrl;
	}

	/**
	 * @param brokerUrl the brokerUrl to set
	 */
	public void setBrokerUrl(String brokerUrl) {
		_brokerUrl = brokerUrl;
	}

}