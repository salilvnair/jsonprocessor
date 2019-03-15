package com.github.salilvnair.jsonprocessor.request.context;

import java.util.Collections;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;

public class JsonValidatorContext {

	private String path;
	private JsonRequest jsonRequest;
	private Map<String,Object> userValidatorMap;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public JsonRequest getJsonRequest() {
		return jsonRequest;
	}

	public void setJsonRequest(JsonRequest baseJsonRequest) {
		this.jsonRequest = baseJsonRequest;
	}

	public Map<String,Object> getUserValidatorMap() {
		if(userValidatorMap==null) {
			return Collections.emptyMap();
		}
		return userValidatorMap;
	}

	public void setUserValidatorMap(Map<String,Object> userValidatorMap) {
		this.userValidatorMap = userValidatorMap;
	}

	
	
}
