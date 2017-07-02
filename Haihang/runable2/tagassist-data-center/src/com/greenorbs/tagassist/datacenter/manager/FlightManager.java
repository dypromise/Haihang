/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.datacenter.manager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FlightTerminate;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightCanceled;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightCheckingIn;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightClosed;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightDepartured;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightRescheduled;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightScheduled;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class FlightManager extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Flight proxy messages */
		this.subscribe(FlightScheduled.class);
		this.subscribe(FlightRescheduled.class);
		this.subscribe(FlightCanceled.class);
		this.subscribe(FlightCheckingIn.class);
		this.subscribe(FlightClosed.class);
		this.subscribe(FlightDepartured.class);
		
		/* Administrator commands */
		this.subscribe(FlightTerminate.class);
	}
	
	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof FlightScheduled) {
			
			FlightScheduled m = (FlightScheduled) message;
			FlightInfo flightInfo = m.getFlightInfo();
			
			this.handleFlightScheduled(flightInfo);
			
		} else if (message instanceof FlightRescheduled) {
			
			FlightRescheduled m = (FlightRescheduled) message;
			FlightInfo flightInfo = m.getFlightInfo();
			
			this.handleFlightStatusChanged(flightInfo, FlightInfo.STATUS_RESCHEDULED);
			
		} else if (message instanceof FlightCanceled) {
			
			FlightCanceled m = (FlightCanceled) message;
			FlightInfo flightInfo = m.getFlightInfo();
			
			this.handleFlightStatusChanged(flightInfo, FlightInfo.STATUS_CANCELED);
			
		} else if (message instanceof FlightCheckingIn) {
			
			FlightCheckingIn m = (FlightCheckingIn) message;
			FlightInfo flightInfo = m.getFlightInfo();
			
			this.handleFlightStatusChanged(flightInfo, FlightInfo.STATUS_CHECKING_IN);
			
		} else if (message instanceof FlightClosed) {
			
			FlightClosed m = (FlightClosed) message;
			FlightInfo flightInfo = m.getFlightInfo();
			
			this.handleFlightStatusChanged(flightInfo, FlightInfo.STATUS_CLOSED);
			
		} else if (message instanceof FlightDepartured) {
			
			FlightDepartured m = (FlightDepartured) message;
			FlightInfo flightInfo = m.getFlightInfo();
			
			this.handleFlightStatusChanged(flightInfo, FlightInfo.STATUS_DEPARTURED);
			
		} else if (message instanceof FlightTerminate) {
			
			FlightTerminate m = (FlightTerminate) message;
			String flightId = m.getFlightId();
			String source = m.getSource();
			
			this.handleFlightStatusChanged(flightId, FlightInfo.STATUS_DEPARTURED, source);
			
		}
		
		return reply;
	}

	private void handleFlightScheduled(FlightInfo flightInfo) {
		if (null == flightInfo || StringUtils.isEmpty(flightInfo.getFlightId())) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			FlightInfo storedFlight = (FlightInfo) session.createQuery(
					"from FlightInfo where flightId='" + flightInfo.getFlightId() + "'").uniqueResult();
			if (null == storedFlight) {
				flightInfo.setStatus(FlightInfo.STATUS_SCHEDULED);
				session.save(flightInfo);
			} else {
				// TODO MARK_1
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
	
	private void handleFlightStatusChanged(FlightInfo flightInfo, int newStatus) {
		if (null == flightInfo || StringUtils.isEmpty(flightInfo.getFlightId())) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			FlightInfo storedFlight = (FlightInfo) session.createQuery(
					"from FlightInfo where flightId='" + flightInfo.getFlightId() + "'").uniqueResult();
			if (storedFlight != null) {
				storedFlight.setStatus(newStatus);
				if (newStatus == FlightInfo.STATUS_RESCHEDULED) {
					storedFlight.setEDT(flightInfo.getEDT());
				} else if (newStatus == FlightInfo.STATUS_DEPARTURED) {
					storedFlight.setADT(flightInfo.getADT());
				}
				session.update(storedFlight);
			} else {
				flightInfo.setStatus(newStatus);
				session.save(flightInfo);
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
	
	private void handleFlightStatusChanged(String flightId, int newStatus, String source) {
		if (StringUtils.isEmpty(flightId)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			FlightInfo storedFlight = (FlightInfo) session.createQuery(
					"from FlightInfo where flightId='" + flightId + "'").uniqueResult();
			if (storedFlight != null) {
				storedFlight.setStatus(newStatus);
				session.update(storedFlight);
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
		
		if (newStatus == FlightInfo.STATUS_SORT_TERMINATED) {
			BaggageManager.handleSortationTerminated(flightId, source);
		} else if (newStatus == FlightInfo.STATUS_DEPARTURED) {
			BaggageManager.handleFlightTerminated(flightId, source);
		}
	}

}
