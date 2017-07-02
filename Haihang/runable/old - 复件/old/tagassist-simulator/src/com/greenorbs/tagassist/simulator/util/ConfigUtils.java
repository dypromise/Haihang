package com.greenorbs.tagassist.simulator.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public class ConfigUtils {

	protected static Logger _log = Logger.getLogger(Configuration.class);

	protected static XMLConfiguration _config;

	public static Configuration instance() {

		if (_config == null) {
			try {
				_config = new XMLConfiguration("config.xml");

			} catch (Exception e) {
				e.printStackTrace();
				_log.fatal("Error to read the configuration file, system exits with code 1");
				System.exit(1);
			}

		}

		return _config;
	}

}
