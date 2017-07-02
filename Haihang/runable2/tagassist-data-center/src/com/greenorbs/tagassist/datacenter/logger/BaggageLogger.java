/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.datacenter.logger;

import java.util.Date;

import com.greenorbs.tagassist.CheckingInfo;
import com.greenorbs.tagassist.IdentificationInfo;
import com.greenorbs.tagassist.TracingInfo;
import com.greenorbs.tagassist.TrackingInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageChecked;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;
import com.greenorbs.tagassist.messagebus.message.WristbandProxyMessages.BaggageIdentified;

public class BaggageLogger {
	
	public static void logBaggageTraced(String baggageNumber, int status, String device) {
		TracingInfo tracingInfo = new TracingInfo();
		tracingInfo.setBaggageNumber(baggageNumber);
		tracingInfo.setStatus(status);
		tracingInfo.setDevice(device);
		tracingInfo.setTime((new Date()).getTime());
		
		HibernateHelper.save(tracingInfo);
	}

	public static void logBaggageChecked(BaggageChecked message) {
		CheckingInfo checkingInfo = new CheckingInfo();
		checkingInfo.setCheckTunnelId(message.getSource());
		checkingInfo.setEPC(message.getEPC());
		checkingInfo.setRssi(message.getRssi());
		checkingInfo.setTime(new Date());
		checkingInfo.setDirection(message.getDirection());
		checkingInfo.setCarriageId(message.getCarriageId());
		
		HibernateHelper.save(checkingInfo);
	}
	
	public static void logBaggageTracked(BaggageTracked message) {
		TrackingInfo trackingInfo = new TrackingInfo();
		trackingInfo.setTrackTunnelId(message.getSource());
		trackingInfo.setEPC(message.getEPC());
		trackingInfo.setRssi(message.getRssi());
		trackingInfo.setTime(new Date());
		trackingInfo.setPoolId(message.getPoolId());
		trackingInfo.setDistance(message.getDistance());
		
		HibernateHelper.save(trackingInfo);
	}
	
	public static void logBaggageIdentified(BaggageIdentified message) {
		IdentificationInfo identificationInfo = new IdentificationInfo();
		identificationInfo.setWristbandId(message.getSource());
		identificationInfo.setEPC(message.getEPC());
		identificationInfo.setRssi(message.getRssi());
		identificationInfo.setTime(new Date());
		
		HibernateHelper.save(identificationInfo);
	}
	
}
