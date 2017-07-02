package com.greenorbs.tagassist.util;

/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Mar 9, 2012
 */

public class BeiyangCRC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// (1B) 00 06 FF FF 84 20 52 00 (13 BC)
		byte[] bytes = new byte[] { 0x00, 0x06, (byte) 0xFF, (byte) 0xFF,
				(byte) 0x84, 0x20, 0x52, 0x00 };

		byte[] data2 = new byte[] { 0x00, 0x0A, (byte) 0xFF, (byte) 0xFF, 
				0x01, 0x05, 0x01, 0x02, 0x02, 0x01, 0x03, 0x01 };

		printHex(bytes);

		byte[] crc = getCRC16(bytes);
		printHex(crc);

		byte[] crc2 = getCRC16(bytes, 0, bytes.length);
		printHex(crc2);
		
		printHex(data2);
		
		byte[] crc3 = getCRC16(data2, 0, data2.length);
		printHex(crc3);
	}

	public static byte[] getCRC16(byte[] bytes, int start, int length) {
		int end = start + length;
		if (end > bytes.length) {
			end = bytes.length;
		}
		
		int currentValue = 0x0000;

		for (int i = start; i < end; i++) {
			currentValue = currentValue ^ (bytes[i] & 0xFF);
			for (int j = 0; j < 8; j++) {
				if ((currentValue & 0x0001) != 0) {
					currentValue = (currentValue >>> 1) ^ 0x8408;
				} else {
					currentValue = (currentValue >>> 1);
				}
			}
		}

		byte b0 = (byte) ((~currentValue >>> 8) & 0xFF);
		byte b1 = (byte) (~currentValue & 0xFF);

		return new byte[] { b0, b1 };

	}

	public static byte[] getCRC16(byte[] bytes) {
		return getCRC16(bytes, 0, bytes.length);
	}

	public static void printHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			sb.append(String.format("%02X ", bytes[i]));
		}

		System.out.println(sb.toString());
	}

}
