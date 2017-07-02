package com.greenorbs.tagassist.webservice;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.message.MobileReaderMessages.BoardingReport;
import com.greenorbs.tagassist.messagebus.util.querier.BaggageQuerier;
import com.greenorbs.tagassist.webservice.util.BusUtils;
import com.greenorbs.tagassist.webservice.util.JsonUtils;

public class BaggageService {
	
	public String getBaggageInfo(String baggageNumber) {
		BaggageQuerier querier = new BaggageQuerier(BusUtils.getPublisher());
		BaggageInfo baggageInfo = querier.getBaggageInfo(baggageNumber);
		
		return JsonUtils.toJson(baggageInfo);
	}
	
	public String[] getBaggageNumberList(String flightId) {
		BaggageQuerier querier = new BaggageQuerier(BusUtils.getPublisher());
		String[] list = querier.getBaggageNumberList(flightId);
		
		return list;
	}
	
	public int getBaggageCount(String flightId) {
		BaggageQuerier querier = new BaggageQuerier(BusUtils.getPublisher());
		BaggageInfo[] list = querier.getBaggageList(flightId);
		
		return (list == null ? 0 : list.length);
	}
	
	public boolean sendBoardingReport(String jsonReport) {
		try {
			BoardingReport report = (BoardingReport) BoardingReport.fromJSON(
					BoardingReport.class.getName(), jsonReport);
			BusUtils.getPublisher().publish(report);
			return true;
		} catch (MessageBusException e) {
			e.printStackTrace();
			return false;
		}
	}

}
