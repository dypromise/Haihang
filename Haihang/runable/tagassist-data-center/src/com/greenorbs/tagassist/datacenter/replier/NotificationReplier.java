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

import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseNotificationInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseNotificationList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryNotificationInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryNotificationList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class NotificationReplier extends BusMessageHandler {
	
	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryNotificationInfo.class);
		this.subscribe(QueryNotificationList.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryNotificationInfo) {
			
			String uuid = ((QueryNotificationInfo) message).getNotificationUUID();
			NotificationInfo notificationInfo = this.getNotificationInfoByUUID(uuid);
			
			reply = new ResponseNotificationInfo(notificationInfo);
			
		} else if (message instanceof QueryNotificationList) {
			
			NotificationInfo[] notificationInfoList = this.getNotificationInfoList();
			
			reply = new ResponseNotificationList(notificationInfoList);
			
		}

		return reply;
	}
	
	private NotificationInfo getNotificationInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		String queryString = "from NotificationInfo where uuid='" + uuid + "'";
		NotificationInfo notificationInfo = (NotificationInfo) HibernateHelper.uniqueQuery(queryString);
		
		return notificationInfo;
	}
	
	private NotificationInfo[] getNotificationInfoList() {
		String queryString = "from NotificationInfo order by time desc";
		ArrayList<NotificationInfo> list = (ArrayList<NotificationInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new NotificationInfo[0]);
		} else {
			return null;
		}
	}
	
}
