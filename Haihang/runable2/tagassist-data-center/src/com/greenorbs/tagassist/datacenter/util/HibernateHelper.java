package com.greenorbs.tagassist.datacenter.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateHelper {
	
	protected static Logger _logger = Logger.getLogger(HibernateHelper.class);

	public static Object uniqueQuery(String queryString) {
		Object result = null;
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			result = session.createQuery(queryString).uniqueResult();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			result = null;
			throw e;
		} finally {
			session.close();
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static List listQuery(String queryString) {
		List result = null;
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			result = session.createQuery(queryString).list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			result = null;
			throw e;
		} finally {
			session.close();
		}
		
		return result;
	}
	
	public static void save(Object obj) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.save(obj);
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e.getMessage());
		} finally {
			session.close();
		}
	}
	
}
