package com.greenorbs.tagassist;

import java.awt.geom.Point2D;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public abstract class Configuration {

	protected static Logger _log = Logger.getLogger(Configuration.class);

	protected static XMLConfiguration _config = new XMLConfiguration();

	static {
		try {
			_config = new XMLConfiguration("config.xml");
		} catch (Exception e) {
			try {
				_config = new XMLConfiguration("/config.xml");
			} catch (Exception e2) {
				_log.fatal("Error to read the configuration file, system exits with code 1.");
				System.exit(1);
			}
		}
	}
	
	public static boolean contains(String path){
		return _config.containsKey(path);
	}
	
	public static Long getLong(String path){
		return _config.getLong(path);
	}

	public static int getInt(String path) {
		return _config.getInt(path);
	}

	public static float getFloat(String path) {
		return _config.getFloat(path);
	}

	public static String getString(String path) {
		return _config.getString(path);
	}
	
	public static boolean getBoolean(String path){
		return _config.getBoolean(path);
	}

	public static Point2D getPoint(String path) {
		try {
			@SuppressWarnings("rawtypes")
			List point = _config.getList(path);
			double x = Double.parseDouble((String) point.get(0));
			double y = Double.parseDouble((String) point.get(1));
			return new Point2D.Double(x, y);
		} catch (Exception ex) {
			ex.printStackTrace();
			_log.fatal("The configuration is not correct. The system will exit with code 1");
			System.exit(1);
			return null;
		}
	}

	public static int getSize(String path) {
		try {
			List<HierarchicalConfiguration> result = _config.configurationsAt(path);
			if (result == null) {
				return 0;
			}
			return result.size();
		} catch (Exception ex) {
			ex.printStackTrace();
			_log.fatal("The configuration is not correct. The system will exit with code 1");
			System.exit(1);
		}
		return 0;
	}

	public static void saveProperty(String path, String value) throws ConfigurationException {

		throw new UnsupportedOperationException(
				"This method is not instable, which may clean the configuration.");

		// if(StringUtils.isBlank(_config.getString(path))==true){
		// throw new ConfigurationException("The path is not existed.");
		// }
		//
		// try {
		// _config.setProperty(path, value);
		// _config.save();
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// _log.error("It fails to change the configuration.");
		// throw new
		// ConfigurationException("It fails to set the configuration for '"+path+"'");
		// }

	}

	public static void removeProperty(String path) throws ConfigurationException {
		throw new UnsupportedOperationException(
				"This method is not instable, which may clean the configuration.");

//		try {
//			_config.clearTree(path);
//			_config.save();
//		} catch (Exception e) {
//			_log.error("It fails to change the configuration.");
//			throw new ConfigurationException("It fails to set the configuration for '" + path + "'");
//		}
	}

}
