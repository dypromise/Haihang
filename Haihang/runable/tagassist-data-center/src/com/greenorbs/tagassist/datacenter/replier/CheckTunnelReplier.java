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

import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCheckTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCheckTunnelList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCheckTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCheckTunnelList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class CheckTunnelReplier extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryCheckTunnelInfo.class);
		this.subscribe(QueryCheckTunnelList.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryCheckTunnelInfo) { 
		
			String checkTunnelUUID = ((QueryCheckTunnelInfo) message).getCheckTunnelId();
			CheckTunnelInfo checkTunnelInfo = this.getCheckTunnelInfoByUUID(checkTunnelUUID);
			
			reply = new ResponseCheckTunnelInfo(checkTunnelInfo);
			
		} else if (message instanceof QueryCheckTunnelList) {
			
			String poolId = ((QueryCheckTunnelList) message).getPoolId();
			CheckTunnelInfo[] checkTunnelInfoList = this.getCheckTunnelInfoListByPoolId(poolId);
			
			reply = new ResponseCheckTunnelList(checkTunnelInfoList);
			
		}
		
		return reply;
	}

	private CheckTunnelInfo getCheckTunnelInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		String queryString = "from CheckTunnelInfo where uuid='" + uuid + "'";
		CheckTunnelInfo checkTunnelInfo = (CheckTunnelInfo) HibernateHelper.uniqueQuery(queryString);
			
		return checkTunnelInfo;
	}
	
	private CheckTunnelInfo[] getCheckTunnelInfoListByPoolId(String poolId) {
		String queryString = "from CheckTunnelInfo";
		if (StringUtils.isNotEmpty(poolId)) {
			queryString += " where locationParam1='" + poolId + "'";
		}
		
		ArrayList<CheckTunnelInfo> list = (ArrayList<CheckTunnelInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new CheckTunnelInfo[0]);
		} else {
			return null;
		}
	}
	
}
