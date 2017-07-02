package com.greenorbs.tagassist.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.log4j.Logger;

public class ObjectUtils {

	private static Logger _log = Logger.getLogger(ObjectUtils.class);

	public static Object newInstance(String className) {
		Object result = null;

		try {
			Class<?> cls = Class.forName(className);
			result = cls.newInstance();
		} catch (ClassNotFoundException e1) {
			_log.fatal("The class '" + className + "' is not found. ");
			e1.printStackTrace();
		} catch (InstantiationException e2) {
			_log.fatal("Cannot to instantiate the instance.");
			e2.printStackTrace();
		} catch (IllegalAccessException e3) {
			_log.fatal("Cannot to new instance since illegal access. ");
			e3.printStackTrace();
		}

		return result;
	}

	public static <T> T newInstance(Class<T> className, Object... parameters) {

		try {
			return ConstructorUtils.invokeConstructor(className, parameters);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			_log.fatal("Cannot to new instance since illegal access. System exits with code 1.");
			System.exit(1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			_log.fatal("No such construction method in the class" + className + ".");
			System.exit(1);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			_log.fatal("InvocationTarget exception." + e);
			System.exit(1);
		}

		return null;
	}
}
