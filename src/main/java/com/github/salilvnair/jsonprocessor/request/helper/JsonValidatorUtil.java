package com.github.salilvnair.jsonprocessor.request.helper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.type.Mode;


public class JsonValidatorUtil {
//	private JsonValidatorContext jsonValidatorContext;
//	private JsonRequest jsonRequest;
//	private List<?> jsonRequestList;
//
//	public JsonValidatorUtil setUserValidatorMap(Map<String,Object> userValidatorMap) {
//		if(this.jsonValidatorContext==null) {
//			this.jsonValidatorContext = new JsonValidatorContext();
//		}
//		jsonValidatorContext.setUserValidatorMap(userValidatorMap);
//		return this;
//	}
//
//	public JsonValidatorUtil setValidValuesDataSet(Map<String,List<String>> validValuesDataSet) {
//		if(this.jsonValidatorContext==null) {
//			this.jsonValidatorContext = new JsonValidatorContext();
//		}
//		jsonValidatorContext.setValidValuesDataSet(validValuesDataSet);
//		return this;
//	}
//
//	public JsonValidatorUtil setUserDefinedMessageDataSet(Map<String,String> userDefinedMessageDataSet) {
//		if(this.jsonValidatorContext==null) {
//			this.jsonValidatorContext = new JsonValidatorContext();
//		}
//		jsonValidatorContext.setUserDefinedMessageDataSet(userDefinedMessageDataSet);
//		return this;
//	}
//
//	public JsonValidatorUtil request(JsonRequest jsonRequest) {
//		this.jsonRequest = jsonRequest;
//		return this;
//	}
//
//	public JsonValidatorUtil mode(Mode validationMode) {
//		if(this.jsonValidatorContext==null) {
//			this.jsonValidatorContext = new JsonValidatorContext();
//		}
//		jsonValidatorContext.setMode(validationMode);
//		return this;
//	}
//
//	public JsonValidatorUtil request(List<?> jsonRequestList) {
//		this.jsonRequestList = jsonRequestList;
//		return this;
//	}
//
//
	public static List<ValidationMessage> validate(JsonValidatorContext validatorContext) {
		JsonRequest jsonRequest = validatorContext.getRootRequest();
		List<?> jsonRequestList = validatorContext.getRootList();
		if(jsonRequest==null && (jsonRequestList==null||jsonRequestList.isEmpty())) {
			return Collections.emptyList();
		}
		JsonProcessorUtil jsonProcessorUtil = new JsonProcessorUtil(validatorContext);
		if(jsonRequestList==null || jsonRequestList.isEmpty()) {
			return jsonProcessorUtil.validate(jsonRequest);
		}
		else {
			return jsonProcessorUtil.validate(jsonRequestList);
		}
	}
	
}
