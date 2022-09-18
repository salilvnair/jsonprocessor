package com.github.salilvnair.jsonprocessor.request.helper;

import java.util.ArrayList;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.factory.JsonValidatorFactory;
import com.github.salilvnair.jsonprocessor.request.type.JsonElementType;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;


public class JsonProcessorUtil {
	
	private List<JsonKeyValidator> jsonKeyValidators;
	
	private JsonValidatorContext jsonValidatorContext;
	
	public static final String ROOT_PATH = "$";
		
	public JsonProcessorUtil(JsonValidatorContext jsonValidatorContext) {
		this.jsonValidatorContext = jsonValidatorContext;
	}
	
	public JsonProcessorUtil(Object object,JsonElementType jsonElementType) {
		this.setJsonRequestValidators(JsonValidatorFactory.generate(object,jsonElementType));
	}
	
	public List<ValidationMessage> validate(JsonRequest request) {
		this.init(request,JsonElementType.OBJECT);
		jsonValidatorContext.setRootRequest(request);
		return this.validate(request, ROOT_PATH,jsonValidatorContext);
	}
	
	public List<ValidationMessage> validate(List<?> request) {
		this.init(request,JsonElementType.LIST);
		jsonValidatorContext.setRootList(request);
		return this.validate(request, ROOT_PATH,jsonValidatorContext);
	}
	
	public List<ValidationMessage> validate(Object requestInstance, String path, JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
        for (JsonKeyValidator v : getJsonRequestValidators()) {
            errors.addAll(v.validate(requestInstance, path,jsonValidatorContext));
        }
        return errors;
	}
	
	public List<JsonKeyValidator> getJsonRequestValidators() {
		return jsonKeyValidators;
	}

	public void setJsonRequestValidators(List<JsonKeyValidator> jsonKeyValidators) {
		this.jsonKeyValidators = jsonKeyValidators;
	}	
	
	private void init(Object object,JsonElementType jsonElementType) {		
		this.setJsonRequestValidators(JsonValidatorFactory.generate(object,jsonElementType));
	}

	public JsonValidatorContext getJsonValidatorContext() {
		return jsonValidatorContext;
	}

	public void setJsonValidatorContext(JsonValidatorContext jsonValidatorContext) {
		this.jsonValidatorContext = jsonValidatorContext;
	}
	
}
