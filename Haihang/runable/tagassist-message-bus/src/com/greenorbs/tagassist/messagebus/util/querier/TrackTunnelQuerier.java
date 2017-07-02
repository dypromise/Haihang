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

import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseTrackTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseTrackTunnelList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryTrackTunnelInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryTrackTunnelList;

public class TrackTunnelQuerier extends BusQuerier {

	public TrackTunnelQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public TrackTunnelInfo getTrackTunnelInfo(String trackTunnelId) {
		QueryTrackTunnelInfo query = new QueryTrackTunnelInfo(trackTunnelId);
		ResponseTrackTunnelInfo response = (ResponseTrackTunnelInfo) this.queryOne(query);
		
		if (response != null && response.getTrackTunnelInfo() != null) {
			return response.getTrackTunnelInfo();
		} else {
			return null;
		}
	}
	
	public TrackTunnelInfo[] getTrackTunnelList(String poolId) {
		QueryTrackTunnelList query = new QueryTrackTunnelList(poolId);
		ResponseTrackTunnelList response = (ResponseTrackTunnelList) this.queryOne(query);
		
		if (response != null && response.getTrackTunnelInfoList() != null) {
			return response.getTrackTunnelInfoList();
		} else {
			return null;
		}
	}

}
