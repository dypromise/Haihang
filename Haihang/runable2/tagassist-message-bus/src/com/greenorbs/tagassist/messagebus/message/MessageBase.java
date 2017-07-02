/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.message;

import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import com.greenorbs.tagassist.JsonIgnoreField;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;

public abstract class MessageBase extends AbstractMessage {
	
	public static final int COMPONENT_FLIGHT_PROXY 		= 1;
	public static final int COMPONENT_DATA_CENTER 		= 2;
	public static final int COMPONENT_WRISTBAND_PROXY 	= 3;
	public static final int COMPONENT_ADMINISTRATOR 	= 4;
	public static final int COMPONENT_TRACK_TUNNEL 		= 5;
	public static final int COMPONENT_CHECK_TUNNEL 		= 6;
	public static final int COMPONENT_MOBILE_READER 	= 7;
	public static final int COMPONENT_VIRTUALIZATION 	= 8;
	
	public static final int COMPONENT_M1 = 1;
	public static final int COMPONENT_M2 = 2;
	public static final int COMPONENT_M3 = 3;
	public static final int COMPONENT_M4 = 4;
	public static final int COMPONENT_M5 = 5;
	public static final int COMPONENT_M6 = 6;
	public static final int COMPONENT_M7 = 7;
	public static final int COMPONENT_M8 = 8;
	
	protected String _messageId;
	
	protected String _source;
	
	protected int _component;
	
	protected String _name;
	
	protected boolean _needConfirm;
	
	protected long _timestamp;
	
	protected Object _custom;
	
	protected String _customClass;
	
	protected String _customJSON;
	
	public MessageBase() {
		this._messageId = UUID.randomUUID().toString();
		this._timestamp = (new Date()).getTime();
	}

	public String getMessageId() {
		return _messageId;
	}
	
	public void setMessageId(String messageId) {
		_messageId = messageId;
	}
	
	public String getSource() {
		return _source;
	}
	
	public void setSource(String source) {
		_source = source;
	}
	
	public int getComponent() {
		return _component;
	}
	
	public void setComponent(int component) {
		_component = component;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}

	public boolean getNeedConfirm() {
		return _needConfirm;
	}

	public void setNeedConfirm(boolean needConfirm) {
		_needConfirm = needConfirm;
	}

	public long getTimestamp() {
		return _timestamp;
	}

	public void setTimestamp(long timestamp) {
		_timestamp = timestamp;
	}
	
	@JsonIgnoreField()
	public Object getCustom() {
		if (null == _custom) {
			try {
				JSONObject jsonObject = JSONObject.fromObject(_customJSON);
				_custom = JSONObject.toBean(jsonObject, Class.forName(_customClass));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return _custom;
	}

	public void setCustom(Object custom) {
		_custom = custom;
		_customClass = (custom != null ? custom.getClass().getName() : null);
		_customJSON = (custom != null ? JSONObject.fromObject(custom).toString() : null);
	}

	public String getCustomClass() {
		return _customClass;
	}

	public void setCustomClass(String customClass) {
		_customClass = customClass;
	}

	public String getCustomJSON() {
		return _customJSON;
	}

	public void setCustomJSON(String customJSON) {
		_customJSON = customJSON;
	}
	
}
