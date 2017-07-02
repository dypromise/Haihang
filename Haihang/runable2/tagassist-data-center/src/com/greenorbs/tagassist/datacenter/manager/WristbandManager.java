/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 10, 2012
 */

package com.greenorbs.tagassist.datacenter.manager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.WristbandBindingInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.WristbandProxyMessages;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class WristbandManager extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Wristband messages */
		this.subscribe(WristbandProxyMessages.BindingChanged.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof WristbandProxyMessages.BindingChanged) {
			
			WristbandProxyMessages.BindingChanged m = (WristbandProxyMessages.BindingChanged) message;
			String wristbandId = m.getSource();
			String flightId = m.getFlightId();
			boolean bound = m.getBound();
			
			this.handleBindingChanged(wristbandId, flightId, bound);
			
		}
		
		return reply;
	}

	private void handleBindingChanged(String wristbandId, String flightId, boolean bound) {
		if (StringUtils.isEmpty(wristbandId) || StringUtils.isEmpty(flightId)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			WristbandBindingInfo wristbandBindingInfo = (WristbandBindingInfo) session.createQuery(
					"from WristbandBindingInfo where wristbandId='" + wristbandId + 
					"' and flightId='" + flightId + "'").uniqueResult();
			
			if (bound) {
				if (null == wristbandBindingInfo) {
					wristbandBindingInfo = new WristbandBindingInfo();
					wristbandBindingInfo.setWristbandId(wristbandId);
					wristbandBindingInfo.setFlightId(flightId);
					session.save(wristbandBindingInfo);
				} else {
					// Duplicated binding
				}
			} else {
				if (wristbandBindingInfo != null) {
					session.delete(wristbandBindingInfo);
				} else {
					// Do nothing
				}
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}
	
}
