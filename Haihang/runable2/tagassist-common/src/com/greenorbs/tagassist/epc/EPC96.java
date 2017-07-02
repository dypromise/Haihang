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

import java.text.ParseException;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.util.EPC96Utils;

public class EPC96 extends EPC {
	
	private static Logger _logger = Logger.getLogger(EPC96.class);
	
	public static final String PREFIX_BIN = "1011";
	public static final String PREFIX_HEX = "B";

	public EPC96(String hex) throws EPCException {
		super(hex);
	}
	
	@Override
	protected int sizeInHex() {
		return 24;
	}

	@Override
	public String getRaw() {
		return EPC96Encoder.binDecode(this.getBin());
	}
	
	public String getBaggageNumber() {
		try {
			return EPC96Utils.parseBaggageNumber(this);
		} catch (ParseException e) {
			_logger.error("Error in parsing " + this.getHex());
			return null;
		}
	}
	
	public String getFlightCode() {
		try {
			return EPC96Utils.parseFlightCode(this);
		} catch (ParseException e) {
			_logger.error("Error in parsing " + this.getHex());
			return null;
		}
	}
	
}
