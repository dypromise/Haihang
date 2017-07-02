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

import com.greenorbs.tagassist.FacilityInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFacilityInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFacilityList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFacilityInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFacilityList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class FacilityReplier extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryFacilityInfo.class);
		this.subscribe(QueryFacilityList.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryFacilityInfo) {
			
			String facilityUUID = ((QueryFacilityInfo) message).getTargetUUID();
			FacilityInfo facilityInfo = this.getFacilityInfoByUUID(facilityUUID);
			
			reply = new ResponseFacilityInfo(facilityInfo);
			
		} else if (message instanceof QueryFacilityList) {
			
			FacilityInfo[] facilityInfoList = this.getFacilityInfoList();
			
			reply = new ResponseFacilityList(facilityInfoList);
			
		}
		
		return reply;
	}

	private FacilityInfo getFacilityInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		String queryString = "from FacilityInfo where uuid='" + uuid + "'";
		FacilityInfo facilityInfo = (FacilityInfo) HibernateHelper.uniqueQuery(queryString);
		
		return facilityInfo;
	}

	private FacilityInfo[] getFacilityInfoList() {
		String queryString = "from FacilityInfo";
		ArrayList<FacilityInfo> list = (ArrayList<FacilityInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new FacilityInfo[0]);
		} else {
			return null;
		}
	}

}
