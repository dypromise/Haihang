/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 24, 2012
 */

package com.greenorbs.tagassist.messagebus.util.querier;

import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseNotificationInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseNotificationList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryNotificationInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryNotificationList;

public class NotificationQuerier extends BusQuerier {

	public NotificationQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public NotificationInfo getNotificationInfo(String notificationId) {
		QueryNotificationInfo query = new QueryNotificationInfo(notificationId);
		ResponseNotificationInfo response = (ResponseNotificationInfo) this.queryOne(query);
		
		if (response != null && response.getNotificationInfo() != null) {
			return response.getNotificationInfo();
		} else {
			return null;
		}
	}

	public NotificationInfo[] getNotificationList() {
		QueryNotificationList query = new QueryNotificationList();
		ResponseNotificationList response = (ResponseNotificationList) this.queryOne(query);
		
		if (response != null && response.getNotificationInfoList() != null) {
			return response.getNotificationInfoList();
		} else {
			return null;
		}
	}
	
}
