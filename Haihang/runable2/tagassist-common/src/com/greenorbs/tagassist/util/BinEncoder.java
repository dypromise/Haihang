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

public class BinEncoder {
	
	private static final String[] _map4 = {
		"0000", "0001", "0010", "0011", "0100", "0101",	"0110", "0111",	"1000", "1001"
	};
	
	private static final String[] _map6 = {
		"000000", "000001", "000010", "000011",	"000100", "000101",	"000110", "000111",
		"001000", "001001",	"001010", "001011",	"001100", "001101",	"001110", "001111",
		"010000", "010001",	"010010", "010011",	"010100", "010101",	"010110", "010111",
		"011000", "011001",	"011010", "011011",	"011100", "011101",	"011110", "011111",
		"100000", "100001",	"100010", "100011"
	};
	
	private static final String _pad4 = "1111";
	private static final String _pad6 = "111111";
	
	private static final String _magic = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
	private static Hashtable<String, String> _demap4 = new Hashtable<String, String>();	
	private static Hashtable<String, String> _demap6 = new Hashtable<String, String>();
	
	static {
		for (int i = 0; i <= 9; i++) {
			_demap4.put(_map4[i], _magic.substring(i, i + 1));
		}
		_demap4.put(_pad4, "");
	
		for (int i = 0; i <= 35; i++) {
			_demap6.put(_map6[i], _magic.substring(i, i + 1));
		}
		_demap6.put(_pad6, "");
	}

	public static String encode4(String s, int toLength) {
		StringBuilder bin = new StringBuilder();
		
		for (char c : s.toCharArray()) {
			bin.append(_map4[c - '0']);
		}
		
		while (bin.length() < toLength * 4) {
			bin.append(_pad4);
		}
		
		return bin.toString();
	}
	
	public static String encode6(String s, int toLength) {
		StringBuilder bin = new StringBuilder();
		
		for (char c : s.toCharArray()) {
			bin.append(_map6[c <= '9' ? c - '0' : c - 'A' + 10]);
		}
		
		while (bin.length() < toLength * 6) {
			bin.append(_pad6);
		}
		
		return bin.toString();
	}
	
	public static String decode4(String bin) {
		StringBuilder raw = new StringBuilder();
		
		for (int i = 0; i < bin.length(); i += 4) {
			raw.append(_demap4.get(bin.substring(i, i + 4)));
		}
		
		return raw.toString();
	}
	
	public static String decode6(String bin) {
		StringBuilder raw = new StringBuilder();
		
		for (int i = 0; i < bin.length(); i += 6) {
			raw.append(_demap6.get(bin.substring(i, i + 6)));
		}
		
		return raw.toString();
	}
	
}
