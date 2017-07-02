/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 13, 2012
 */

package com.greenorbs.tagassist.flightproxy.dcsadapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.greenorbs.tagassist.util.BaggageUtils;

public class DCSBlockParser {
	
	protected static Logger _logger = Logger.getLogger(DCSBlockParser.class);
	
	private static Pattern _nSegmentPattern = Pattern.compile("\\.N/([0-9]{10})([0-9]{3})");
	private static Pattern _fLinePattern = Pattern.compile("\\.F/(([^/]*)/[^/]*)/([^/]*)/(.*)");
	private static Pattern _wLinePattern = Pattern.compile("\\.W/[^/]*/([^/]*)/([^/]*)(/[^/]*)?");

	public static ArrayList<DCSMessage> parseBlock(DCSBlock block) {
		if (block.isBSMBlock()) {
			switch (block.getVersion()) {
			case DCSBlock.VERSION_A:
			case DCSBlock.VERSION_B:
			case DCSBlock.VERSION_C:
			case DCSBlock.VERSION_1:
			{
				return parseBSMBlock(block);
			}
			default:
				return null;
			}
		}
		
		return null;
	}
	
	private static ArrayList<DCSMessage> parseBSMBlock(DCSBlock block) {
		ArrayList<DCSMessage> result = new ArrayList<DCSMessage>();
		boolean markDel = false;
		
		DCSMessage message = new DCSMessage(DCSMessage.TYPE_SOURCE);
		
		if (block.isBSMBlock()) {
			for (String line : block.getLines()) {
				if (line.startsWith("DEL")) {
					/*
					 * A DEL mark indicates the removal of baggage.
					 */
					markDel = true;
				} else if (line.startsWith(".N/")) {
					/*
					 * A single ".N/" line may hold multiple baggage for the 
					 * same passenger, each identified by a ".N/" segment;
					 * also, there may be multiple ".N/" lines corresponding
					 * to the same passenger.
					 */
					String[] segs = line.split(" ");
					for (String seg : segs) {
						if (seg.startsWith(".N/")) {
							Matcher m = _nSegmentPattern.matcher(seg);
							if (m.matches()) {
								long base = Long.parseLong(m.group(1));
								int n = Integer.parseInt(m.group(2));
								for (int i = 0; i < n; i++) {
									String baggageNumber = String.format("%010d", base + i);
									// Make sure we have done the right thing.
									if (BaggageUtils.isValid(baggageNumber)) {
										_logger.debug("Baggage number generated: " + baggageNumber);
										message.getBaggageNumberList().add(baggageNumber);
									} else {
										_logger.error("Invalid baggage number generated: " + baggageNumber +
												" CHECK YOUR CODE!");
									}
								}
							} else {
								_logger.warn("Invalid \".N/\" segment encountered: " + seg);
							}
						}
					}
				} else if (line.startsWith(".F/")) {
					/*
					 * In version B, we may encounter multiple ".F/" lines
					 * for the same passenger, only the first of which 
					 * indicates the flight we should concern.
					 */
					if (StringUtils.isBlank(message.getFlightId())) {
						Matcher m = _fLinePattern.matcher(line);
						if (m.matches()) {
							String flightId = m.group(1);
							int year = (Calendar.getInstance()).get(Calendar.YEAR);
							int dayOfYear = (Calendar.getInstance()).get(Calendar.DAY_OF_YEAR);
							if (flightId.endsWith("31DEC") && dayOfYear == 1) {
								year = year - 1;
							}
							message.setFlightCode(m.group(2));
							message.setFlightId(flightId + year);
							message.setDestination(m.group(3));
							message.setBClass(m.group(4));
						} else {
							_logger.warn("Invalid \".F/\" line encountered: " + line);
						}
					}
				} else if (line.startsWith(".P/")) {
					/*
					 * In all versions, a ".P/" line indicates the end of
					 * a sub-block.
					 */
					String passenger = line.substring(3).replace("/", " ").trim();
					if (StringUtils.isBlank(message.getPassenger())) {
						message.setPassenger(passenger);
					} else {
						_logger.warn("Unexpected \".P/\" line encountered: " + line);
					}
					
					/*
					 * If everything is good.
					 */
					if (!message.getBaggageNumberList().isEmpty() && 
							StringUtils.isNotBlank(message.getFlightId()) &&
							StringUtils.isNotBlank(message.getPassenger()))
					{
						result.add(message);
					} else {
						_logger.warn("Incomplete sub-block detected.");
					}
					
					/*
					 * Anyway, we should start working with a new sub-block.
					 */
					message = new DCSMessage(DCSMessage.TYPE_SOURCE);
				} else if (line.startsWith(".W/")) {
					/*
					 * In version 1 (only), a ".W/" line indicates the total 
					 * weight of baggage in a sub-block.
					 */
					if (message.getBaggageWeight() == 0) {
						Matcher m = _wLinePattern.matcher(line);
						if (m.matches()) {
							try {
								message.setBaggageCount(Integer.parseInt(m.group(1)));
								message.setBaggageWeight(Integer.parseInt(m.group(2)));
							} catch (Exception e) {
								_logger.warn("Invalid \".W/\" line encountered: " + line);
							}
						}
					} else {
						_logger.warn("Unexpected \".W/\" line encountered: " + line);
					}
				}
			}
		}
		
		if (markDel) {
			for (DCSMessage dcsMessage : result) {
				dcsMessage.setType(DCSMessage.TYPE_REMOVAL);
			}
		}
		
		return result;
	}
	
}
