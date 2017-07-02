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

import org.apache.commons.lang3.Validate;

import com.greenorbs.tagassist.util.HexHelper;

public abstract class EPC {
	
	private String _hex;
	
	public EPC(String hex) throws EPCException {
		try {
			Validate.notNull(hex);
			Validate.isTrue(hex.length() <= sizeInHex());
			Validate.isTrue(hex.matches("[0-9A-F]{" + sizeInHex() + "}"));
		} catch (Exception e) {
			throw new EPCException("Invalid EPC hex encountered.");
		}
		
		StringBuilder sb = new StringBuilder(hex);
		sb.ensureCapacity(sizeInHex());
		for (int i = hex.length(); i < sizeInHex(); i++) {
			sb.append("0");
		}
		
		_hex = sb.toString();
	}
	
	@Override
	public String toString() {
		return _hex;
	}
	
	public String getHex() {
		return _hex;
	}
	
	public String getBin() {
		return HexHelper.hexToBin(_hex);
	}
	
	public abstract String getRaw();
	
	protected abstract int sizeInHex();
	
}
