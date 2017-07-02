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

import java.util.Hashtable;

import org.apache.commons.lang.Validate;

public class HexHelper {

	private static final String _magicHex = "0123456789ABCDEF";

	private static final String[] _magicBin = {
		"0000", "0001", "0010", "0011",	"0100", "0101", "0110", "0111", 
		"1000", "1001", "1010", "1011",	"1100", "1101", "1110", "1111"
	};

	private static Hashtable<Character, String> _h2bMap = new Hashtable<Character, String>();
	private static Hashtable<String, Character> _b2hMap = new Hashtable<String, Character>();
	private static Hashtable<Character, Byte> _h2BMap = new Hashtable<Character, Byte>();
	private static Hashtable<Byte, Character> _B2hMap = new Hashtable<Byte, Character>();

	static {
		for (int i = 0; i < 16; i++) {
			_h2bMap.put(_magicHex.charAt(i), _magicBin[i]);
			_b2hMap.put(_magicBin[i], _magicHex.charAt(i));
			_h2BMap.put(_magicHex.charAt(i), (byte) i);
			_B2hMap.put((byte) i, _magicHex.charAt(i));
		}
	}

	public static String hexToBin(String hexString) {
		StringBuilder binString = new StringBuilder(hexString.length() * 4);
		for (int i = 0; i < hexString.length(); i++) {
			binString.append(_h2bMap.get(hexString.charAt(i)));
		}
		return binString.toString();
	}

	public static String binToHex(String binString) {
		StringBuilder hexString = new StringBuilder(binString.length() / 4);
		for (int i = 0; i < binString.length(); i += 4) {
			hexString.append(_b2hMap.get(binString.substring(i, i + 4)));
		}
		return hexString.toString();
	}

	public static byte[] hexToByte(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			byte high = _h2BMap.get(hexString.charAt(i));
			byte low = _h2BMap.get(hexString.charAt(i + 1));
			bytes[i / 2] = (byte) ((high << 4) | low);
		}
		return bytes;
	}

	public static int hexToInt(String hexString) {
		byte[] bb = hexToByte(hexString);
		return (int) ((((bb[0] & 0xff) << 24) | ((bb[1] & 0xff) << 16)
				| ((bb[2] & 0xff) << 8) | ((bb[3] & 0xff) << 0)));
	}

	public static String byteToHex(byte[] bytes) {
		return byteToHex(bytes, 0, bytes.length);
	}

	public static String byteToHex(byte[] bytes, int start, int length) {
		Validate.isTrue(bytes != null && start >= 0 && start < bytes.length
				&& length >= 0);
		int end = start + length;
		if (end > bytes.length) {
			end = bytes.length;
		}
		StringBuilder hexString = new StringBuilder(length * 2);
		for (int i = start; i < end; i++) {
			hexString.append(_B2hMap.get((byte) ((bytes[i] & 0xF0) >> 4)));
			hexString.append(_B2hMap.get((byte) (bytes[i] & 0x0F)));
		}
		return hexString.toString();
	}

	public static String toReadable(byte[] bytes) {
		return toReadable(bytes, 0, bytes.length);
	}

	public static String toReadable(byte[] bytes, int start, int length) {
		Validate.isTrue(bytes != null && start >= 0 && start < bytes.length
				&& length >= 0);
		int end = start + length;
		if (end > bytes.length) {
			end = bytes.length;
		}
		StringBuilder readable = new StringBuilder(bytes.length * 3);
		for (int i = start; i < end; i++) {
			readable.append(String.format("%02X ", bytes[i]));
		}
		return readable.toString().trim();
	}

	public static void printHex(byte[] bytes) {
		printHex(bytes, 0, bytes.length);
	}

	public static void printHex(byte[] bytes, int start, int length) {
		System.out.println(toReadable(bytes, start, length));
	}

}
