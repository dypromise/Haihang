package com.greenorbs.tagassist.storage.treap;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.storage.treap.disk.DiskTreap;
import com.greenorbs.tagassist.util.ObjectUtils;

public class TreapDB {

	private static ITreap _instance;

	private final static String DEFAULT_CACHE_PATH = "temp/storage";
	private final static int DEFAULT_BLOCK_SIZE = 256;
	private final static long DEFAULT_MEMORY_SIZE = 64 << 20;

	private static Logger _log = Logger.getLogger(TreapDB.class);

	@SuppressWarnings("rawtypes")
	public static ITreap instance() {
		if (_instance == null) {

			String cachePath = DEFAULT_CACHE_PATH;
			int blockSize = DEFAULT_BLOCK_SIZE;
			long mmapSize = DEFAULT_MEMORY_SIZE;
			boolean startClear = true;

			try {
				XMLConfiguration config = new XMLConfiguration("config.xml");
				config.setExpressionEngine(new XPathExpressionEngine());

				if (config.containsKey("storages/@cache")) {
					cachePath = config.getString("storages/@cache");
				} else {
					_log.warn("No cache path set in the config.xml. The default cache path is used.");
				}

				if (config.containsKey("storages/@blockSize")) {
					blockSize = config.getInt("storages/@blockSize");
				} else {
					_log.warn("no block size set in the config.xml. The default value is used.");
				}

				if (config.containsKey("storages/@mmapSize")) {
					mmapSize = config.getLong("storages/@mmapSize");
				} else {
					_log.warn("no memory size set in the config.xml. The default value is used.");
				}

				if (config.containsKey("storages/@startClear")) {
					startClear = config.getBoolean("storages/@startClear");
				} else {
					_log.warn("no start clear set in the config.xml. The default value is used.");
				}

			} catch (ConfigurationException e) {
				_log.error("It fails to read configuration from the config.xml. All parameters will be given by the default values.");
				e.printStackTrace();
			}

			try {
				File file = new File(cachePath);
				if (startClear && file.exists()) {
					deleteFile(file);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			try {

				_instance = new DiskTreap(blockSize, new File(cachePath
						+ "/storage"), mmapSize);

			} catch (Exception e) {
				e.printStackTrace();
				_log.error("It fails to new the DiskTreap instance. The system will exit.");
				System.exit(0);
			}

		}

		return _instance;
	}

	public static void dump() {

		int length = instance().length();

		Map content = instance().kmax(length);

		System.out.println(content);
	}

	private static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}

	}

	public static void main(String[] args) throws Exception {

		DOMConfigurator.configure("log4j.xml");
		TreapDB.instance();
		//
		for (int i = 0; i < 1000; i++) {
			TreapDB.instance().put(i, i + 1);

		}

		System.out.println(62 << 20);

	}
}
