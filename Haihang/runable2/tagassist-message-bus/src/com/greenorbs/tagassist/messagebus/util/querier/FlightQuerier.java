/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 15, 2012
 */

package com.greenorbs.tagassist.messagebus.util.querier;

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFlightInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFlightList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFlightInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFlightList;

public class FlightQuerier extends BusQuerier {
	
	public FlightQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public FlightInfo getFlightInfo(String flightId) {
		QueryFlightInfo query = new QueryFlightInfo(flightId);
		ResponseFlightInfo response = (ResponseFlightInfo) this.queryOne(query);
		
		if (response != null && response.getFlightInfo() != null) {
			return response.getFlightInfo();
		} else {
			return null;
		}
	}

	public FlightInfo[] getFlightList() {
		QueryFlightList query = new QueryFlightList();
		ResponseFlightList response = (ResponseFlightList) this.queryOne(query);
		
		if (response != null && response.getFlightInfoList() != null) {
			return response.getFlightInfoList();
		} else {
			return null;
		}
	}
	
}
