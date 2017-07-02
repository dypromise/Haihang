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

import com.greenorbs.tagassist.FacilityInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFacilityInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseFacilityList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFacilityInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryFacilityList;

public class FacilityQuerier extends BusQuerier {

	public FacilityQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public FacilityInfo getFacilityInfo(String facilityUUID) {
		QueryFacilityInfo query = new QueryFacilityInfo(facilityUUID);
		ResponseFacilityInfo response = (ResponseFacilityInfo) this.queryOne(query);
		
		if (response != null && response.getFacilityInfo() != null) {
			return response.getFacilityInfo();
		} else {
			return null;
		}
	}
	
	public FacilityInfo[] getFacilityList() {
		QueryFacilityList query = new QueryFacilityList();
		ResponseFacilityList response = (ResponseFacilityList) this.queryOne(query);
		
		if (response != null && response.getFacilityInfoList() != null) {
			return response.getFacilityInfoList();
		} else {
			return null;
		}
	}

}
