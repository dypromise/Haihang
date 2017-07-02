/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.test;

import com.greenorbs.tagassist.JsonIgnoreField;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;

public class TestMessage extends AbstractMessage implements Cloneable {
	
	private int _intField;
	private String _stringField;
	private boolean _booleanField;
	private Object _ignoreField;
	
	public int getIntField() {
		return _intField;
	}
	
	public void setIntField(int intField) {
		_intField = intField;
	}
	
	public String getStringField() {
		return _stringField;
	}
	
	public void setStringField(String stringField) {
		_stringField = stringField;
	}
	
	public boolean getBooleanField() {
		return _booleanField;
	}
	
	public void setBooleanField(boolean booleanField) {
		_booleanField = booleanField;
	}
	
	@JsonIgnoreField
	public Object getObjectField() {
		return _ignoreField;
	}

	public void setObjectField(Object ignoreField) {
		_ignoreField = ignoreField;
	}

	@Override
	public TestMessage clone() throws CloneNotSupportedException {
		return (TestMessage) super.clone();
	}
	
}
