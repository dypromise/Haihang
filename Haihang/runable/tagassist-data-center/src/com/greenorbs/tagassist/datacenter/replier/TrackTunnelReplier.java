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

import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseTrackTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseTrackTunnelList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryTrackTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryTrackTunnelList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class TrackTunnelReplier extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryTrackTunnelInfo.class);
		this.subscribe(QueryTrackTunnelList.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryTrackTunnelInfo) {
			
			String trackTunnelUUID = ((QueryTrackTunnelInfo) message).getTrackTunnelId();
			TrackTunnelInfo trackTunnelInfo = this.getTrackTunnelInfoByUUID(trackTunnelUUID);
			
			reply = new ResponseTrackTunnelInfo(trackTunnelInfo);
			
		} else if (message instanceof QueryTrackTunnelList) {
			
			String poolId = ((QueryTrackTunnelList) message).getPoolId();
			TrackTunnelInfo[] trackTunnelInfoList = this.getTrackTunnelInfoListByPoolId(poolId);
			
			reply = new ResponseTrackTunnelList(trackTunnelInfoList);
			
		}
		
		return reply;
	}

	private TrackTunnelInfo getTrackTunnelInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		String queryString = "from TrackTunnelInfo where uuid='" + uuid + "'";
		TrackTunnelInfo trackTunnelInfo = (TrackTunnelInfo) HibernateHelper.uniqueQuery(queryString);
		
		return trackTunnelInfo;
	}
	
	private TrackTunnelInfo[] getTrackTunnelInfoListByPoolId(String poolId) {
		String queryString = "from TrackTunnelInfo";
		if (StringUtils.isNotEmpty(poolId)) {
			queryString += " where locationParam1='" + poolId + "'";
		}
		
		ArrayList<TrackTunnelInfo> list = (ArrayList<TrackTunnelInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new TrackTunnelInfo[0]);
		} else {
			return null;
		}
	}
	
}
