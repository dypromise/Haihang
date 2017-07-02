package com.greenorbs.tagassist.datacenter.replier;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCarriageInfo;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseCarriageList;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCarriageInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryCarriageList;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class CarriageReplier extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Queries */
		this.subscribe(QueryCarriageInfo.class);
		this.subscribe(QueryCarriageList.class);
	}
	
	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryCarriageInfo) {
			
			String carriageId = ((QueryCarriageInfo) message).getCarriageId();
			CarriageInfo carriageInfo = this.getCarriageInfoByUUID(carriageId);
			
			reply = new ResponseCarriageInfo(carriageInfo);
			
		} else if (message instanceof QueryCarriageList) {
			
			CarriageInfo[] carriageInfoList = this.getCarriageInfoList();
			
			reply = new ResponseCarriageList(carriageInfoList);
			
		}
		
		return reply;
	}

	private CarriageInfo getCarriageInfoByUUID(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		String queryString = "from CarriageInfo where uuid='" + uuid + "'";
		CarriageInfo carriageInfo = (CarriageInfo) HibernateHelper.uniqueQuery(queryString);
		
		return carriageInfo;
	}
	
	private CarriageInfo[] getCarriageInfoList() {
		String queryString = "from CarriageInfo";
		ArrayList<CarriageInfo> list = (ArrayList<CarriageInfo>) HibernateHelper.listQuery(queryString);
		
		if (list != null) {
			return list.toArray(new CarriageInfo[0]);
		} else {
			return null;
		}
	}
	
}
