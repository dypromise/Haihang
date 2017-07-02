/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.datacenter.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;

public class HibernateTest {
	
	SessionFactory _sessionFactory;

	@Before
	public void setUp() throws Exception {
	    _sessionFactory = HibernateUtils.getSessionFactory();
	}

	@Test
	public void test() {
		/*
		 * Delete
		 */
		Session session = _sessionFactory.openSession();
		session.beginTransaction();
		session.createQuery("delete BaggageInfo where number='NUM1234567890'").executeUpdate();
		session.createQuery("delete FlightInfo where flightId='ZLY402/14JAN'").executeUpdate();
		session.getTransaction().commit();
		session.close();
		
		/*
		 * Insert
		 */
		session = _sessionFactory.openSession();
		session.beginTransaction();
		
		FlightInfo flight = new FlightInfo();
		Calendar c = Calendar.getInstance();
		c.set(2012, 1, 14);
		flight.setArriveTime(c.getTimeInMillis());
		c.add(Calendar.HOUR_OF_DAY, 2);
		flight.setDepartTime(c.getTimeInMillis());
		flight.setFlightId("ZLY402/14JAN");
		session.save(flight);
		
		BaggageInfo baggage = new BaggageInfo();
		baggage.setBClass("T");
		baggage.setDamageCode(BaggageInfo.DAMAGE_PERFECT);
		baggage.setEPC("EPC1234567890");
		baggage.setFlightId("ZLY402/14JAN");
		baggage.setNumber("NUM1234567890");
		baggage.setPassenger("Mr. ZLY");
		baggage.setWeight(30);
		baggage.setStatus(0);
		session.save(baggage);
		
		session.getTransaction().commit();
		session.close();
		
		/*
		 * Query
		 */
		session = _sessionFactory.openSession();
		session.beginTransaction();	
		List<BaggageInfo> result = session.createQuery("from BaggageInfo where number='NUM1234567890'").list();
		session.getTransaction().commit();
		session.close();
		
		assertEquals(1, result.size());
		assertEquals("EPC1234567890", ((BaggageInfo) result.get(0)).getEPC());
	}

}
