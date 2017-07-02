package com.greenorbs.tagassist.webservice;

import java.util.ArrayList;

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.messagebus.util.querier.FlightQuerier;
import com.greenorbs.tagassist.webservice.util.BusUtils;

public class FlightService {
	
	public String[] getFlightIdList() {
		FlightQuerier querier = new FlightQuerier(BusUtils.getPublisher());
		FlightInfo[] flightList = querier.getFlightList();
		
		if (flightList != null) {
			ArrayList<String> flightIdList = new ArrayList<String>(flightList.length);
			for (FlightInfo flightInfo : flightList) {
				flightIdList.add(flightInfo.getFlightId());
			}
			return flightIdList.toArray(new String[0]);
		} else {
			return null;
		}
	}

}
