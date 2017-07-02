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

import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class CheckTunnelManager extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Check-tunnel messages */
		this.subscribe(CheckTunnelMessages.BindingChanged.class);
		this.subscribe(CheckTunnelMessages.CarriageChanged.class);
		this.subscribe(CheckTunnelMessages.LocationMoved.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof CheckTunnelMessages.BindingChanged) {
			
			CheckTunnelMessages.BindingChanged m = (CheckTunnelMessages.BindingChanged) message;
			String checkTunnelUUID = m.getSource();
			String flightId = m.getFlightId();
			boolean bound = m.getBound();
			
			this.handleBindingChanged(checkTunnelUUID, flightId, bound);
			
		} else if (message instanceof CheckTunnelMessages.CarriageChanged) {
			
			CheckTunnelMessages.CarriageChanged m = (CheckTunnelMessages.CarriageChanged) message;
			String checkTunnelUUID = m.getSource();
			String carriageId = m.getCarriageId();
			boolean bound = m.getBound();
			
			this.handleCarriageChanged(checkTunnelUUID, carriageId, bound);
			
		} else if (message instanceof CheckTunnelMessages.LocationMoved) {
			
			CheckTunnelMessages.LocationMoved m = (CheckTunnelMessages.LocationMoved) message;
			String checkTunnelUUID = m.getSource();
			String param1 = m.getLocationParam1();
			Integer param2 = m.getLocationParam2();
			Float param3 = m.getLocationParam3();
			
			this.updateLocation(checkTunnelUUID, param1, param2, param3);
			
		}
		
		return reply;
	}

	private void handleBindingChanged(String checkTunnelUUID, String flightId, boolean bound) {
		if (StringUtils.isEmpty(checkTunnelUUID) || StringUtils.isEmpty(flightId)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
		
			CheckTunnelInfo checkTunnelInfo = (CheckTunnelInfo) session.createQuery(
					"from CheckTunnelInfo where uuid='" + checkTunnelUUID + "'").uniqueResult();
			if (null == checkTunnelInfo) {
				checkTunnelInfo = new CheckTunnelInfo();
				checkTunnelInfo.setUUID(checkTunnelUUID);
			}
			checkTunnelInfo.setBoundFlightId(bound ? flightId : null);
			session.saveOrUpdate(checkTunnelInfo);
			
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
	
	private void handleCarriageChanged(String checkTunnelUUID, String carriageId, boolean bound) {
		if (StringUtils.isEmpty(checkTunnelUUID) || StringUtils.isEmpty(carriageId)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
		
			CheckTunnelInfo checkTunnelInfo = (CheckTunnelInfo) session.createQuery(
					"from CheckTunnelInfo where uuid='" + checkTunnelUUID + "'").uniqueResult();
			if (null == checkTunnelInfo) {
				checkTunnelInfo = new CheckTunnelInfo();
				checkTunnelInfo.setUUID(checkTunnelUUID);
			}
			checkTunnelInfo.setBoundCarriageId(bound ? carriageId : null);
			session.saveOrUpdate(checkTunnelInfo);
			
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

	private void updateLocation(String checkTunnelUUID, String param1, Integer param2, Float param3) {
		if (StringUtils.isEmpty(checkTunnelUUID)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			CheckTunnelInfo checkTunnelInfo = (CheckTunnelInfo) session.createQuery(
					"from CheckTunnelInfo where uuid='" + checkTunnelUUID + "'").uniqueResult();
			if (null == checkTunnelInfo) {
				checkTunnelInfo = new CheckTunnelInfo();
				checkTunnelInfo.setUUID(checkTunnelUUID);
			}
			checkTunnelInfo.setLocationParam1(param1);
			checkTunnelInfo.setLocationParam2(param2);
			checkTunnelInfo.setLocationParam3(param3);
			session.saveOrUpdate(checkTunnelInfo);
			
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
