/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.io;

import javax.jms.Destination;

import com.greenorbs.tagassist.JsonIgnoreField;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public abstract class AbstractMessage {
	
	private static JsonConfig _jsonConfig;
	
	private static JsonConfig getJsonConfig() {
		if (null == _jsonConfig) {
			_jsonConfig = new JsonConfig();
			_jsonConfig.addIgnoreFieldAnnotation(JsonIgnoreField.class);
		}
		return _jsonConfig;
	}
	
	private Destination _destination;
	
	private Destination _replyTo;

	public static AbstractMessage fromJSON(String className, String json) {
		AbstractMessage message = null;
			
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			message = (AbstractMessage) JSONObject.toBean(jsonObject, Class.forName(className));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return message;
	}

	public String toJSON() {
		JSONObject jsonObject = JSONObject.fromObject(this, getJsonConfig());
		String json = jsonObject.toString();

		return json;
	}

	protected Destination getDestination() {
		return _destination;
	}

	protected void setDestination(Destination destination) {
		_destination = destination;
	}

	protected Destination getReplyTo() {
		return _replyTo;
	}

	protected void setReplyTo(Destination replyTo) {
		_replyTo = replyTo;
	}
	
	@Override
	public String toString() {
		return MessageHelper.getTopicName(this) + ": " + this.toJSON();
	}
	
}
