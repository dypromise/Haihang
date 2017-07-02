/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 3, 2012
 */

package com.greenorbs.tagassist.epc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;

import com.greenorbs.tagassist.util.BinEncoder;
import com.greenorbs.tagassist.util.HexHelper;

public class EPC96Encoder {
	
	private static Pattern _rawPattern = Pattern.compile("([0-9]{10})([0-9A-Z]{1,2}[A-Z])([0-9]{3,4})([A-Z]?)");
	private static Pattern _binPattern = Pattern.compile(EPC96.PREFIX_BIN + "([01]{52})([01]{18})([01]{16})([01]{6})");
	private static Pattern _hexPattern = Pattern.compile(EPC96.PREFIX_HEX + "[0-9A-F]{23}");

	public static String binEncode(String raw) {
		raw = raw.toUpperCase();
		
		Matcher m = _rawPattern.matcher(raw);
		Validate.isTrue(m.matches());
		
		StringBuilder bin = new StringBuilder();
		
		bin.append(EPC96.PREFIX_BIN)
			.append(BinEncoder.encode4(m.group(1), 13))
			.append(BinEncoder.encode6(m.group(2), 3))
			.append(BinEncoder.encode4(m.group(3), 4))
			.append(BinEncoder.encode6(m.group(4), 1));
		
		return bin.toString();
	}
	
	public static String binDecode(String bin) {
		Matcher m = _binPattern.matcher(bin);
		Validate.isTrue(m.matches());
		
		bin = bin.substring(EPC96.PREFIX_BIN.length());
		
		StringBuilder raw = new StringBuilder();
		
		raw.append(BinEncoder.decode4(bin.substring(0, 52)))
			.append(BinEncoder.decode6(bin.substring(52, 70)))
			.append(BinEncoder.decode4(bin.substring(70, 86)))
			.append(BinEncoder.decode6(bin.substring(86, 92)));
		
		m = _rawPattern.matcher(raw);
		Validate.isTrue(m.matches());
		
		return raw.toString();
	}
	
	public static String hexEncode(String raw) {
		return HexHelper.binToHex(binEncode(raw));
	}
	
	public static String hexDecode(String hex) {
		Matcher m = _hexPattern.matcher(hex);
		Validate.isTrue(m.matches());
		
		return binDecode(HexHelper.hexToBin(hex));
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: EPC96Encoder.jar <baggageNumber> <flightNumber>");
			System.exit(1);
		}
		
		String baggageNumber = args[0];
		String flightNumber = args[1];
		System.out.println(EPC96Encoder.hexEncode(baggageNumber+flightNumber));
	}
	
}
