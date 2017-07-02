package com.greenorbs.tagassist.flightproxy.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.flightproxy.FlightProxy;

public class FlightProxyTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FlightProxy.instance().start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FlightProxy.instance().close();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
