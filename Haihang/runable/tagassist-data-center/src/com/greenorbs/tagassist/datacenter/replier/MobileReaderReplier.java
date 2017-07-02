/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 23, 2012
 */

package com.greenorbs.tagassist.datacenter.replier;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.greenorbs.tagassist.MobileReaderInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseMobileReaderInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseMobileReaderList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryMobileReaderInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryMobileReaderList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class MobileReaderReplier extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryMobileReaderInfo.class);
		this.subscribe(QueryMobileReaderList.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryMobileReaderInfo) {
			
			String mobileReaderUUID = ((QueryMobileReaderInfo) message).getMobileReaderId();
			MobileReaderInfo mobileReaderInfo = this.getMobileReaderInfoByUUID(mobileReaderUUID);
			
			reply = new ResponseMobileReaderInfo(mobileReaderInfo);
			
		} else if (message instanceof QueryMobileReaderList) {
			
			MobileReaderInfo[] mobileReaderInfoList = this.getMobileReaderInfoList();
			
			reply = new ResponseMobileReaderList(mobileReaderInfoList);
			
		}
		
		return reply;
	}

	private MobileReaderInfo getMobileReaderInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		String queryString = "from MobileReaderInfo where uuid='" + uuid + "'";
		MobileReaderInfo mobileReaderInfo = (MobileReaderInfo) HibernateHelper.uniqueQuery(queryString);
		
		return mobileReaderInfo;
	}

	private MobileReaderInfo[] getMobileReaderInfoList() {
		String queryString = "from MobileReaderInfo";
		ArrayList<MobileReaderInfo> list = (ArrayList<MobileReaderInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new MobileReaderInfo[0]);
		} else {
			return null;
		}
	}

}
