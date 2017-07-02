package com.greenorbs.tagassist.webservice.test;

import org.junit.Test;

import com.greenorbs.tagassist.webservice.FlightService;

public class FlightServiceTest {

	@Test
	public void testGetFlightIdList() {
		FlightService flightService = new FlightService();
		String[] list = flightService.getFlightIdList();
		for (String id : list) {
			System.out.println(id);
		}
	}

}
