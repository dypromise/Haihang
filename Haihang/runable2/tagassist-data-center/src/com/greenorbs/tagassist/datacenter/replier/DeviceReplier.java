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

import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseDeviceList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryDeviceList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class DeviceReplier extends BusMessageHandler {
	
	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryDeviceInfo.class);
		this.subscribe(QueryDeviceList.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryDeviceInfo) {
			
			String deviceUUID = ((QueryDeviceInfo) message).getTargetUUID();
			DeviceInfo deviceInfo = this.getDeviceInfoByUUID(deviceUUID);
			
			reply = new ResponseDeviceInfo(deviceInfo);
			
		} else if (message instanceof QueryDeviceList) {
			
			DeviceInfo[] deviceInfoList = this.getDeviceInfoList();

			reply = new ResponseDeviceList(deviceInfoList);
		}
			
		return reply;
	}

	private DeviceInfo getDeviceInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		String queryString = "from DeviceInfo where uuid='" + uuid + "'";
		DeviceInfo deviceInfo = (DeviceInfo) HibernateHelper.uniqueQuery(queryString);
		
		return deviceInfo;
	}

	private DeviceInfo[] getDeviceInfoList() {
		String queryString = "from DeviceInfo";
		ArrayList<DeviceInfo> list = (ArrayList<DeviceInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new DeviceInfo[0]);
		} else {
			return null;
		}
	}
	
}
