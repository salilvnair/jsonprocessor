package com.github.salilvnair.jsonprocessor.request.context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.type.Mode;

public class JsonValidatorContext {
	private String id;
	private String path;
	private JsonRequest jsonRequest;
	private JsonRequest rootRequest;
	private List<?> rootList;
	private Map<String,Object> userValidatorMap;
	private Map<String,List<String>> validValuesDataSet;
	private Map<String,String> userDefinedMessageDataSet;
	private Field field;
	private Field parent;
	private Mode mode = Mode.STRICT;
	private Map<String, List<PathInfoContext>> fieldPathInfo;

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
			userValidatorMap = new HashMap<>();
		}
		return userValidatorMap;
	}

	public void setUserValidatorMap(Map<String,Object> userValidatorMap) {
		this.userValidatorMap = userValidatorMap;
	}

	public Map<String,List<String>> getValidValuesDataSet() {
		if(validValuesDataSet==null) {
			validValuesDataSet = new HashMap<>();
		}
		return validValuesDataSet;
	}

	public void setValidValuesDataSet(Map<String,List<String>> validValuesDataSet) {
		this.validValuesDataSet = validValuesDataSet;
	}

	public Map<String,String> getUserDefinedMessageDataSet() {
		if(userDefinedMessageDataSet==null) {
			userDefinedMessageDataSet = new HashMap<>();
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
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Map<String, List<PathInfoContext>> getFieldPathInfo() {
		if(fieldPathInfo==null) {
			fieldPathInfo = new HashMap<>();
		}
		return fieldPathInfo;
	}

	public void setFieldPathInfo(Map<String, List<PathInfoContext>> fieldPathInfo) {
		this.fieldPathInfo = fieldPathInfo;
	}

	public Field getParent() {
		return parent;
	}

	public void setParent(Field parent) {
		this.parent = parent;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode validationMode) {
		this.mode = validationMode;
	}	

}
