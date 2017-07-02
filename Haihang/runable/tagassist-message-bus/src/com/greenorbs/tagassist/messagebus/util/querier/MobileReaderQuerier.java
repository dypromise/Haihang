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

import com.greenorbs.tagassist.MobileReaderInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseMobileReaderInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseMobileReaderList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryMobileReaderInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryMobileReaderList;

public class MobileReaderQuerier extends BusQuerier {

	public MobileReaderQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public MobileReaderInfo getMobileReaderInfo(String mobileReaderId) {
		QueryMobileReaderInfo query = new QueryMobileReaderInfo(mobileReaderId);
		ResponseMobileReaderInfo response = (ResponseMobileReaderInfo) this.queryOne(query);
		
		if (response != null && response.getMobileReaderInfo() != null) {
			return response.getMobileReaderInfo();
		} else {
			return null;
		}
	}
	
	public MobileReaderInfo[] getMobileReaderList() {
		QueryMobileReaderList query = new QueryMobileReaderList();
		ResponseMobileReaderList response = (ResponseMobileReaderList) this.queryOne(query);
		
		if (response != null && response.getMobileReaderInfoList() != null) {
			return response.getMobileReaderInfoList();
		} else {
			return null;
		}
	}

}
