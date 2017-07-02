/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 22, 2012
 */

package com.greenorbs.tagassist.datacenter.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	
	private static final SessionFactory _sessionFactory = buildSessionFactory();
	 
	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {
			// load from different directory
			SessionFactory sessionFactory = new Configuration()
				.configure("hibernate.cfg.xml")
				.buildSessionFactory();
 
			return sessionFactory;
 
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
 
	public static SessionFactory getSessionFactory() {
		return _sessionFactory;
	}
 
	public static void closeSessionFactory() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}
