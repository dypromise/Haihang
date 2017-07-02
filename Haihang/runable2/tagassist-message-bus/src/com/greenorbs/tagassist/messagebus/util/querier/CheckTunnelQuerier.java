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

import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCheckTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCheckTunnelList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCheckTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCheckTunnelList;

public class CheckTunnelQuerier extends BusQuerier {

	public CheckTunnelQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public CheckTunnelInfo getCheckTunnelInfo(String checkTunnelId) {
		QueryCheckTunnelInfo query = new QueryCheckTunnelInfo(checkTunnelId);
		ResponseCheckTunnelInfo response = (ResponseCheckTunnelInfo) this.queryOne(query);
		
		if (response != null && response.getCheckTunnelInfo() != null) {
			return response.getCheckTunnelInfo();
		} else {
			return null;
		}
	}
	
	public CheckTunnelInfo[] getCheckTunnelList(String poolId) {
		QueryCheckTunnelList query = new QueryCheckTunnelList(poolId);
		ResponseCheckTunnelList response = (ResponseCheckTunnelList) this.queryOne(query);
		
		if (response != null && response.getCheckTunnelInfoList() != null) {
			return response.getCheckTunnelInfoList();
		} else {
			return null;
		}
	}

}
