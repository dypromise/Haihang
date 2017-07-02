/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 3, 2012
 */

package com.greenorbs.tagassist.util;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.greenorbs.tagassist.epc.EPC96;
import com.greenorbs.tagassist.epc.EPC96Encoder;

public class EPC96Utils {

	private static final Pattern _rawPattern = Pattern.compile("([0-9]{10})([0-9A-Z]{1,2}[A-Z][0-9]{3,4}[A-Z]?)");
	
	public static String parseBaggageNumber(EPC96 epc) throws ParseException {
		String raw = epc.getRaw();
		
		Matcher m  = _rawPattern.matcher(raw);
		if (m.matches()) {
			return m.group(1);
		} else {
			throw new ParseException(raw, 0);
		}
	}
	
	public static String parseFlightCode(EPC96 epc) throws ParseException {
		String raw = epc.getRaw();
		
		Matcher m  = _rawPattern.matcher(raw);
		if (m.matches()) {
			return m.group(2);
		} else {
			throw new ParseException(raw, 0);
		}
	}
	
	public static String hexEncode(String baggageNumber, String flightCode) {
		return EPC96Encoder.hexEncode(baggageNumber + flightCode);
	}
	
	public static boolean isValid(String epc) {
		try {
			EPC96Encoder.hexDecode(epc);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
