package com.greenorbs.tagassist.webservice.util;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.greenorbs.tagassist.JsonIgnoreField;

public class JsonUtils {

	private static JsonConfig _jsonConfig;

	private static JsonConfig getJsonConfig() {
		if (null == _jsonConfig) {
			_jsonConfig = new JsonConfig();
			_jsonConfig.addIgnoreFieldAnnotation(JsonIgnoreField.class);
		}
		return _jsonConfig;
	}
	
	public static String toJson(Object obj) {
		JSONObject jsonObject = JSONObject.fromObject(obj, getJsonConfig());
		String json = jsonObject.toString();

		return json;
	}

}
