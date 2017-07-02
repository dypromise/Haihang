package com.greenorbs.tagassist.webservice.test;

import org.junit.Test;

import com.greenorbs.tagassist.webservice.BaggageService;

public class BaggageServiceTest {

	@Test
	public void testGetBaggageInfo() {
		BaggageService baggageService = new BaggageService();
		String jsonBaggageInfo = baggageService.getBaggageInfo("3880721865");
		System.out.println(jsonBaggageInfo);
	}

}
