/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 27, 2012
 */

package com.greenorbs.tagassist.messagebus.util.querier;

import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseDeviceList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryDeviceInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryDeviceList;

public class DeviceQuerier extends BusQuerier {

	public DeviceQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public DeviceInfo getDeviceInfo(String deviceUUID) {
		QueryDeviceInfo query = new QueryDeviceInfo(deviceUUID);
		ResponseDeviceInfo response = (ResponseDeviceInfo) this.queryOne(query);
		
		if (response != null && response.getDeviceInfo() != null) {
			return response.getDeviceInfo();
		} else {
			return null;
		}
	}
	
	public DeviceInfo[] getDeviceList() {
		QueryDeviceList query = new QueryDeviceList();
		ResponseDeviceList response = (ResponseDeviceList) this.queryOne(query);
		
		if (response != null && response.getDeviceInfoList() != null) {
			return response.getDeviceInfoList();
		} else {
			return null;
		}
	}

}
