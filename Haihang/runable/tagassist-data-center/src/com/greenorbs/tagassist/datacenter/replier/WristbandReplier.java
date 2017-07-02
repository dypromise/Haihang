/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 10, 2012
 */

package com.greenorbs.tagassist.datacenter.replier;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.WristbandInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseWristbandInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseWristbandList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryWristbandInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryWristbandList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class WristbandReplier extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryWristbandInfo.class);
		this.subscribe(QueryWristbandList.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryWristbandInfo) {
			
			String wristbandUUID = ((QueryWristbandInfo) message).getWristbandId();
			WristbandInfo wristbandInfo = this.getWristbandInfoByUUID(wristbandUUID);
			
			reply = new ResponseWristbandInfo(wristbandInfo);
			
		} else if (message instanceof QueryWristbandList) {
			
			String poolId = ((QueryWristbandList) message).getPoolId();
			WristbandInfo[] wristbandInfoList = this.getWristbandInfoListByPoolId(poolId);
			
			reply = new ResponseWristbandList(wristbandInfoList);
		}
		
		return reply;
	}

	private WristbandInfo getWristbandInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		WristbandInfo wristbandInfo = null;
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			wristbandInfo = (WristbandInfo) session.createQuery(
					"from WristbandInfo where uuid='" + uuid + "'").uniqueResult();
			if (wristbandInfo != null) {
				String[] boundFlightIdList = this.getWristbandBoundFlightIdList(session, uuid);
				wristbandInfo.setBoundFlightIdList(boundFlightIdList);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			wristbandInfo = null;
			
			e.printStackTrace();
			_logger.error(e.getMessage());
		} finally {
			session.close();
		}
		
		return wristbandInfo;
	}

	private WristbandInfo[] getWristbandInfoListByPoolId(String poolId) {
		ArrayList<WristbandInfo> list = null;
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			String queryString = "from WristbandInfo";
			if (StringUtils.isNotEmpty(poolId)) {
				// FIXME Yet nothing we can do right now (Feb 15, 2012)
			}
			list = (ArrayList<WristbandInfo>) session.createQuery(queryString).list();
			if (list != null && !list.isEmpty()) {
				for (WristbandInfo wristbandInfo : list) {
					String[] boundFlightIdList = this.getWristbandBoundFlightIdList(
							session, wristbandInfo.getUUID());
					wristbandInfo.setBoundFlightIdList(boundFlightIdList);
				}
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			list = null;
			
			e.printStackTrace();
			_logger.error(e.getMessage());
		} finally {
			session.close();
		}
		
		if (list != null) {
			return list.toArray(new WristbandInfo[0]);
		} else {
			return null;
		}
	}
	
	private String[] getWristbandBoundFlightIdList(Session session, String wristbandUUID) {
		ArrayList<String> list = (ArrayList<String>) session.createQuery(
				"select flightId from WristbandBindingInfo where wristbandId='" 
				+ wristbandUUID + "'").list();
		
		if (list != null) {
			return list.toArray(new String[0]);
		} else {
			return null;
		}
	}
	
}
