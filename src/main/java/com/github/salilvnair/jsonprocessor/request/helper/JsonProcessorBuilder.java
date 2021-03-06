package com.github.salilvnair.jsonprocessor.request.helper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.type.Mode;


public class JsonProcessorBuilder {
	
	private JsonValidatorContext jsonValidatorContext;
	private JsonProcessorUtil jsonProcessorUtil;
	private JsonRequest jsonRequest;
	private List<?> jsonRequestList;
	
	public JsonProcessorBuilder setUserValidatorMap(Map<String,Object> userValidatorMap) {
		if(this.jsonValidatorContext==null) {
			this.jsonValidatorContext = new JsonValidatorContext();
		}
		jsonValidatorContext.setUserValidatorMap(userValidatorMap);
		return this;
	}
	
	public JsonProcessorBuilder setValidValuesDataSet(Map<String,List<String>> validValuesDataSet) {
		if(this.jsonValidatorContext==null) {
			this.jsonValidatorContext = new JsonValidatorContext();
		}
		jsonValidatorContext.setValidValuesDataSet(validValuesDataSet);
		return this;
	}
	
	public JsonProcessorBuilder setUserDefinedMessageDataSet(Map<String,String> userDefinedMessageDataSet) {
		if(this.jsonValidatorContext==null) {
			this.jsonValidatorContext = new JsonValidatorContext();
		}
		jsonValidatorContext.setUserDefinedMessageDataSet(userDefinedMessageDataSet);
		return this;
	}
	
	public JsonProcessorBuilder request(JsonRequest jsonRequest) {
		this.jsonRequest = jsonRequest;
		return this;
	}
	
	public JsonProcessorBuilder mode(Mode validationMode) {
		if(this.jsonValidatorContext==null) {
			this.jsonValidatorContext = new JsonValidatorContext();
		}
		jsonValidatorContext.setMode(validationMode);
		return this;
	}
	
	public JsonProcessorBuilder request(List<?> jsonRequestList) {
		this.jsonRequestList = jsonRequestList;
		return this;
	}
	
	public List<ValidationMessage> validate() {
		if(jsonRequest==null && (this.jsonRequestList==null||this.jsonRequestList.isEmpty())) {
			return Collections.emptyList();
		}
		if(this.jsonValidatorContext==null) {
			this.jsonValidatorContext = new JsonValidatorContext();
		}
		this.jsonProcessorUtil = new JsonProcessorUtil(jsonValidatorContext);
		if(this.jsonRequestList==null||this.jsonRequestList.isEmpty()) {
			return this.jsonProcessorUtil.validate(jsonRequest);
		}
		else {
			return this.jsonProcessorUtil.validate(jsonRequestList);
		}
	}
	
}
