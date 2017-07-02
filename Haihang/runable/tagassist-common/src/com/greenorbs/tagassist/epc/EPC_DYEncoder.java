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

import com.greenorbs.tagassist.util.ASCII_table;
import com.greenorbs.tagassist.util.BinEncoder;
import com.greenorbs.tagassist.util.HexHelper;

public class EPC_DYEncoder {
	
	private static Pattern _rawPattern = Pattern.compile("([0-9]{10})");
	public static String str2hex(String str) {
	
		Matcher m = _rawPattern.matcher(str);
		Validate.isTrue(m.matches());
		StringBuffer tmp = new StringBuffer();
		for(int i = 0;i<10;i++){
			tmp.append(ASCII_table.ASCII_table_reverse.get(String.valueOf(m.group(1).charAt(i))));
		}
		return tmp.toString();
	}
	
	
	public static void main(String[] args) {
		EPC_DYEncoder encoder = new EPC_DYEncoder();
		
		String baggageNumber = "3859205874";
	
		System.out.println(EPC_DYEncoder.str2hex(baggageNumber));
	}
	
}
