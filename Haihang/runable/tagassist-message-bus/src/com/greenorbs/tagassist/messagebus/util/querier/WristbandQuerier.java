/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 23, 2012
 */

package com.greenorbs.tagassist.messagebus.util.querier;

import com.greenorbs.tagassist.WristbandInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseWristbandInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseWristbandList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryWristbandInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryWristbandList;

public class WristbandQuerier extends BusQuerier {

	public WristbandQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public WristbandInfo getWristbandInfo(String wristbandId) {
		QueryWristbandInfo query = new QueryWristbandInfo(wristbandId);
		ResponseWristbandInfo response = (ResponseWristbandInfo) this.queryOne(query);
		
		if (response != null && response.getWristbandInfo() != null) {
			return response.getWristbandInfo();
		} else {
			return null;
		}
	}
	
	public WristbandInfo[] getWristbandList(String poolId) {
		QueryWristbandList query = new QueryWristbandList(poolId);
		ResponseWristbandList response = (ResponseWristbandList) this.queryOne(query);
		
		if (response != null && response.getWristbandInfoList() != null) {
			return response.getWristbandInfoList();
		} else {
			return null;
		}
	}

}
