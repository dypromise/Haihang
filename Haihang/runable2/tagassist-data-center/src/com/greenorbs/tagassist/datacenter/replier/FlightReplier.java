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

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFlightInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFlightList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFlightInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFlightList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class FlightReplier extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryFlightInfo.class);
		this.subscribe(QueryFlightList.class);
	}
	
	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryFlightInfo) {
			
			String flightId = ((QueryFlightInfo) message).getFlightId();
			FlightInfo flightInfo = this.getFlightInfoByFlightId(flightId);
			
			reply = new ResponseFlightInfo(flightInfo);
			
		} else if (message instanceof QueryFlightList) {
			
			FlightInfo[] flightInfoList = this.getFlightInfoList();
			
			reply = new ResponseFlightList(flightInfoList);
			
		}
		
		return reply;
	}

	private FlightInfo getFlightInfoByFlightId(String flightId) {
		if (StringUtils.isEmpty(flightId)) {
			return null;
		}
		
		String queryString = "from FlightInfo where flightId='" + flightId + "'";
		FlightInfo flightInfo = (FlightInfo) HibernateHelper.uniqueQuery(queryString);
		
		return flightInfo;
	}
	
	private FlightInfo[] getFlightInfoList() {
		String queryString = "from FlightInfo where status!=" + FlightInfo.STATUS_DEPARTURED;
		ArrayList<FlightInfo> list = (ArrayList<FlightInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new FlightInfo[0]);
		} else {
			return null;
		}
	}

}
