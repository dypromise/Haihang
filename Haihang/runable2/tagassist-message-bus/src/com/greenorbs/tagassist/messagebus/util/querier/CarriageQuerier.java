package com.greenorbs.tagassist.messagebus.util.querier;

import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCarriageInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCarriageList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCarriageInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCarriageList;

public class CarriageQuerier extends BusQuerier {
	
	public CarriageQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public CarriageInfo getCarriageInfo(String carriageId) {
		QueryCarriageInfo query = new QueryCarriageInfo(carriageId);
		ResponseCarriageInfo response = (ResponseCarriageInfo) this.queryOne(query);
		
		if (response != null && response.getCarriageInfo() != null) {
			return response.getCarriageInfo();
		} else {
			return null;
		}
	}

	public CarriageInfo[] getCarriageList() {
		QueryCarriageList query = new QueryCarriageList();
		ResponseCarriageList response = (ResponseCarriageList) this.queryOne(query);
		
		if (response != null && response.getCarriageInfoList() != null) {
			return response.getCarriageInfoList();
		} else {
			return null;
		}
	}

}
