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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;

import org.apache.commons.lang3.Validate;

public class FlightUtils {
	
	private static Pattern _flightIdPattern = Pattern.compile("(([0-9A-Z]{1,2}[A-Z])([0-9]{3,4})([A-Z]?))/.*");
	private static Pattern _flightCodePattern = Pattern.compile("([0-9A-Z]{1,2}[A-Z])([0-9]{3,4})([A-Z]?)");
	
	public static boolean isValid(String flightId) {
		Matcher m = _flightIdPattern.matcher(flightId);
		return m.matches();
	}
	
	public static String parseFlightCode(String flightId) throws ParseException {
		Matcher m = _flightIdPattern.matcher(flightId);
		if (m.matches()) {
			return m.group(1);
		} else {
			throw new ParseException(flightId, 0);
		}
	}
	
	public static String parseAirline(String flightId) throws ParseException {
		Matcher m = _flightIdPattern.matcher(flightId);
		if (m.matches()) {
			return m.group(2);
		} else {
			throw new ParseException(flightId, 0);
		}
	}

	public static byte[] encodeFlightCode(String flightCode) {
		flightCode = flightCode.toUpperCase();
		
		Matcher m = _flightCodePattern.matcher(flightCode);
		Validate.isTrue(m.matches());
		
		StringBuilder bin = new StringBuilder();
		
		bin.append(BinEncoder.encode6(m.group(1), 3))
			.append(BinEncoder.encode4(m.group(2), 4))
			.append(BinEncoder.encode6(m.group(3), 1));
		
		byte[] bytes = HexHelper.hexToByte(HexHelper.binToHex(bin.toString()));
		
		return bytes;
	}
	
	public static String decodeFlightCode(byte[] bytes) {
		String bin = HexHelper.hexToBin(HexHelper.byteToHex(bytes));
		
		StringBuilder flightCode = new StringBuilder();
		
		flightCode.append(BinEncoder.decode6(bin.substring(0, 18)))
			.append(BinEncoder.decode4(bin.substring(18, 34)))
			.append(BinEncoder.decode6(bin.substring(34, 40)));
		
		return flightCode.toString();
	}
	
}
