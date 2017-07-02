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

import java.util.Map;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.TracingInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageList;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageNumberList;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseBaggageTrace;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageInfoByEPC;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageNumberList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryBaggageTrace;

public class BaggageQuerier extends BusQuerier {
	
	public BaggageQuerier(Publisher publisher) {
		super(publisher);
	}

	public BaggageInfo getBaggageInfo(String baggageNumber) {
		QueryBaggageInfo query = new QueryBaggageInfo(baggageNumber);
		ResponseBaggageInfo response = (ResponseBaggageInfo) this.queryOne(query);
		
		if (response != null && response.getBaggageInfo() != null) {
			return response.getBaggageInfo();
		} else {
			return null;
		}
	}
	
	public BaggageInfo getBaggageInfoByEPC(String epc) {
		QueryBaggageInfoByEPC query = new QueryBaggageInfoByEPC(epc);
		ResponseBaggageInfo response = (ResponseBaggageInfo) this.queryOne(query);
		
		if (response != null && response.getBaggageInfo() != null) {
			return response.getBaggageInfo();
		} else {
			return null;
		}
	}

	public BaggageInfo[] getBaggageList(String flightId) {
		QueryBaggageList query = new QueryBaggageList(flightId);
		ResponseBaggageList response = (ResponseBaggageList) this.queryOne(query);
		
		if (response != null && response.getBaggageInfoList() != null) {
			return response.getBaggageInfoList();
		} else {
			return null;
		}
	}
	
	public String[] getBaggageNumberList(String flightId) {
		QueryBaggageNumberList query = new QueryBaggageNumberList(flightId);
		ResponseBaggageNumberList response = (ResponseBaggageNumberList) this.queryOne(query);
		
		if (response != null && response.getBaggageNumberList() != null) {
			return response.getBaggageNumberList();
		} else {
			return null;
		}
	}
	
	public TracingInfo[] getBaggageTrace(String baggageNumber) {
		QueryBaggageTrace query = new QueryBaggageTrace(baggageNumber);
		ResponseBaggageTrace response = (ResponseBaggageTrace) this.queryOne(query);
		
		if (response != null && response.getTracingInfoList() != null) {
			return response.getTracingInfoList();
		} else {
			return null;
		}
	}
	
	/*
	 * return whole baggage list
	 * if baggageinfo is null, the existed one is ok
	 * else the return one is ok
	 */
	public Map<String, BaggageInfo> getDifferentBaggageMap(String flightId, Map<String, Long> baggageIndex){
		//TODO add a query in message bus
		return null;
	}
	
}
