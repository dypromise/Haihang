/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Aug 6, 2012
 */

package com.greenorbs.tagassist.flightproxy.dcsadapter;

import com.greenorbs.tagassist.flightproxy.FlightProxyConfiguration;

public class FlightsFilter {

	public static boolean allow(String flightCode) {
		return (FlightProxyConfiguration.allowedFlights().isEmpty() ||
				FlightProxyConfiguration.allowedFlights().contains(flightCode));
	}
	
}
