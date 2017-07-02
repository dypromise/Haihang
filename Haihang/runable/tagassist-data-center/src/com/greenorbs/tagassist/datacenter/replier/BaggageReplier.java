/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.datacenter.replier;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.TracingInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageCheckTunnel;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageList;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageNumberList;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageTrace;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageCheckTunnel;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageInfoByEPC;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageNumberList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageTrace;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class BaggageReplier extends BusMessageHandler {
	
	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryBaggageInfo.class);
		this.subscribe(QueryBaggageInfoByEPC.class);
		this.subscribe(QueryBaggageList.class);
		this.subscribe(QueryBaggageCheckTunnel.class);
		this.subscribe(QueryBaggageNumberList.class);
		this.subscribe(QueryBaggageTrace.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryBaggageInfo) {
			
			String baggageNumber = ((QueryBaggageInfo) message).getBaggageNumber();
			BaggageInfo baggageInfo = this.getBaggageInfoByBaggageNumber(baggageNumber);
			
			reply = new ResponseBaggageInfo(baggageInfo);
			
		} else if (message instanceof QueryBaggageInfoByEPC) {
			
			String epc = ((QueryBaggageInfoByEPC) message).getEPC();
			BaggageInfo baggageInfo = this.getBaggageInfoByEPC(epc);
			
			reply = new ResponseBaggageInfo(baggageInfo);
			
		} else if (message instanceof QueryBaggageList) {
			
			String flightId = ((QueryBaggageList) message).getFlightId();
			BaggageInfo[] baggageInfoList = BaggageReplier.getBaggageInfoListByFlightId(flightId);
			
			reply = new ResponseBaggageList(baggageInfoList);
			
		} else if (message instanceof QueryBaggageCheckTunnel) {
			
			String baggageNumber = ((QueryBaggageCheckTunnel) message).getBaggageNumber();
			CheckTunnelInfo checkTunnelInfo = this.getCheckTunnelInfoByBaggageNumber(baggageNumber);
			
			if (checkTunnelInfo != null) {
				reply = new ResponseBaggageCheckTunnel(checkTunnelInfo.getUUID(), 
						checkTunnelInfo.getDeviceInfo().getName());
			}
			
		} else if (message instanceof QueryBaggageNumberList) {
			
			String flightId = ((QueryBaggageNumberList) message).getFlightId();
			String[] baggageNumberList = this.getBaggageNumberListByFlightId(flightId);
			
			reply = new ResponseBaggageNumberList(flightId, baggageNumberList);
			
		} else if (message instanceof QueryBaggageTrace) {
			
			String baggageNumber = ((QueryBaggageTrace) message).getBaggageNumber();
			TracingInfo[] tracingInfoList = this.getTracingInfoListByBaggageNumber(baggageNumber);
			
			reply = new ResponseBaggageTrace(tracingInfoList);
		}
		
		return reply;
	}

	private BaggageInfo getBaggageInfoByBaggageNumber(String baggageNumber) {
		if (StringUtils.isEmpty(baggageNumber)) {
			return null;
		}
		
		String queryString = "from BaggageInfo where number='" + baggageNumber + "'";
		BaggageInfo baggageInfo = (BaggageInfo) HibernateHelper.uniqueQuery(queryString);
		
		return baggageInfo;
	}
	
	private BaggageInfo getBaggageInfoByEPC(String epc) {
		if (StringUtils.isEmpty(epc)) {
			return null;
		}
		
		String queryString = "from BaggageInfo where epc='" + epc + "'";
		BaggageInfo baggageInfo = (BaggageInfo) HibernateHelper.uniqueQuery(queryString);
		
		return baggageInfo;
	}
	
	public static BaggageInfo[] getBaggageInfoListByFlightId(String flightId) {
		if (StringUtils.isEmpty(flightId)) {
			return null;
		}
		
		String queryString = "from BaggageInfo where flightId='" + flightId + "'";
		ArrayList<BaggageInfo> list = (ArrayList<BaggageInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new BaggageInfo[0]);
		} else {
			return null;
		}
	}
	
	private CheckTunnelInfo getCheckTunnelInfoByBaggageNumber(String baggageNumber) {
		if (StringUtils.isEmpty(baggageNumber)) {
			return null;
		}
		
		CheckTunnelInfo checkTunnelInfo = null;
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			String flightId = (String) session.createQuery(
					"select flightId from BaggageInfo where number='" + baggageNumber + "'").uniqueResult();
			checkTunnelInfo = (CheckTunnelInfo) session.createQuery(
					"from CheckTunnelInfo where boundFlightId='" + flightId + "'").uniqueResult();
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			checkTunnelInfo = null;
			
			e.printStackTrace();
			_logger.error(e.getMessage());
		} finally {
			session.close();
		}
		
		return checkTunnelInfo;
	}
	
	private String[] getBaggageNumberListByFlightId(String flightId) {
		if (StringUtils.isEmpty(flightId)) {
			return null;
		}
		
		String queryString = "select number from BaggageInfo where flightId='" + flightId + "'";
		ArrayList<String> list = (ArrayList<String>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new String[0]);
		} else {
			return null;
		}
	}
	
	private TracingInfo[] getTracingInfoListByBaggageNumber(String baggageNumber) {
		if (StringUtils.isEmpty(baggageNumber)) {
			return null;
		}
		
		String queryString = "from TracingInfo where baggageNumber='" + baggageNumber + "'";
		ArrayList<TracingInfo> list = (ArrayList<TracingInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new TracingInfo[0]);
		} else {
			return null;
		}
	}

}
