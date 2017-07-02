/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 28, 2012
 */

package com.greenorbs.tagassist.flightproxy.dcsadapter;

import java.util.ArrayList;

public class DCSBlock {
	
	public static final int VERSION_UNKNOWN = 0x0;
	public static final int VERSION_A = 0xA;
	public static final int VERSION_B = 0xB;
	public static final int VERSION_C = 0xC;
	public static final int VERSION_1 = 0x1;
	
	public static final String TOKEN_BSM = "BSM";
	public static final String TOKEN_HOLD = "HOLD";

	private String _token;

	/* Line format: <line> := <key>/<value> */
	private ArrayList<String> _lines;
	
	public DCSBlock(String token) {
		_token = token;
		_lines = new ArrayList<String>();
	}
	
	public void appendLine(String line) {
		_lines.add(line);
	}
	
	public String getUniqueValue(String key) {
		for (String line : _lines) {
			if (line.startsWith(key + "/")) {
				return line.substring(line.indexOf("/") + 1);
			}
		}
		return null;
	}
	
	public int getVersion() {
		String value = this.getUniqueValue(".V");
		if (value != null) {
			if (value.startsWith("A")) {
				return VERSION_A;
			} else if (value.startsWith("B")) {
				return VERSION_B;
			} else if (value.startsWith("C")) {
				return VERSION_C;
			} else if (value.startsWith("1")) {
				return VERSION_1;
			}
		}
		return VERSION_UNKNOWN;
	}
	
	public String getToken() {
		return _token;
	}

	public void setToken(String token) {
		_token = token;
	}
	
	public String getEndToken() {
		return "END" + _token;
	}

	public ArrayList<String> getLines() {
		return _lines;
	}

	public void setLines(ArrayList<String> lines) {
		_lines = lines;
	}
	
	public boolean isBSMBlock() {
		return _token.equals(TOKEN_BSM);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append(this.getToken()).append("\n");
		for (String line : this.getLines()) {
			s.append(line).append("\n");
		}
		s.append(this.getEndToken());
		
		return s.toString();
	}
	
}
