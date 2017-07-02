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

public class EPC256 extends EPC {

	public EPC256(String hex) throws EPCException {
		super(hex);
	}

	@Override
	protected int sizeInHex() {
		return 64;
	}

	@Override
	public String getRaw() {
		// TODO Auto-generated method stub
		return null;
	}

}
