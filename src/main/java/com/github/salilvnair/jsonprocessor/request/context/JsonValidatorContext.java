package com.github.salilvnair.jsonprocessor.request.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;

public class JsonValidatorContext {

	private String path;
	private JsonRequest jsonRequest;
	private JsonRequest rootRequest;
	private List<?> rootList;
	private Map<String,Object> userValidatorMap;
	private Map<String,List<String>> validValuesDataSet;
	private Map<String,String> userDefinedMessageDataSet;
	private Field field;

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

	public Map<String,List<String>> getValidValuesDataSet() {
		if(validValuesDataSet==null) {
			return Collections.emptyMap();
		}
		return validValuesDataSet;
	}

	public void setValidValuesDataSet(Map<String,List<String>> validValuesDataSet) {
		this.validValuesDataSet = validValuesDataSet;
	}

	public Map<String,String> getUserDefinedMessageDataSet() {
		if(userDefinedMessageDataSet==null) {
			return Collections.emptyMap();
		}
		return userDefinedMessageDataSet;
	}

	public void setUserDefinedMessageDataSet(Map<String,String> userDefinedMessageDataSet) {
		this.userDefinedMessageDataSet = userDefinedMessageDataSet;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public JsonRequest getRootRequest() {
		return rootRequest;
	}

	public void setRootRequest(JsonRequest rootRequest) {
		this.rootRequest = rootRequest;
	}

	public List<?> getRootList() {
		return rootList;
	}

	public void setRootList(List<?> rootList) {
		this.rootList = rootList;
	}	

}
