/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 23, 2012
 */

package com.greenorbs.tagassist.datacenter.manager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.MobileReaderInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.MobileReaderMessages;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class MobileReaderManager extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Mobile reader messages */
		this.subscribe(MobileReaderMessages.BindingChanged.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof MobileReaderMessages.BindingChanged) {
			
			MobileReaderMessages.BindingChanged m = (MobileReaderMessages.BindingChanged) message;
			String mobileReaderUUID = m.getSource();
			String flightId = m.getFlightId();
			boolean bound = m.getBound();
			
			this.handleBindingChanged(mobileReaderUUID, flightId, bound);
			
		}
		
		return reply;
	}

	private void handleBindingChanged(String mobileReaderUUID, String flightId, boolean bound) {
		if (StringUtils.isEmpty(mobileReaderUUID) || StringUtils.isEmpty(flightId)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
		
			MobileReaderInfo mobileReaderInfo = (MobileReaderInfo) session.createQuery(
					"from MobileReaderInfo where uuid='" + mobileReaderUUID + "'").uniqueResult();
			if (null == mobileReaderInfo) {
				mobileReaderInfo = new MobileReaderInfo();
				mobileReaderInfo.setUUID(mobileReaderUUID);
			}
			mobileReaderInfo.setBoundFlightId(bound ? flightId : null);
			session.saveOrUpdate(mobileReaderInfo);
			
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
