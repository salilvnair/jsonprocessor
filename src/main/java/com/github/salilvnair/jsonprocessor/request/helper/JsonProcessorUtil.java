package com.github.salilvnair.jsonprocessor.request.helper;

import java.util.ArrayList;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.factory.JsonValidatorFactory;
import com.github.salilvnair.jsonprocessor.request.type.JsonElementType;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;


public class JsonProcessorUtil {
	
	private List<JsonRequestValidator> jsonRequestValidators;
	
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
		return this.validate(request, ROOT_PATH,jsonValidatorContext);
	}
	
	public List<ValidationMessage> validate(List<?> request) {
		this.init(request,JsonElementType.LIST);
		return this.validate(request, ROOT_PATH,jsonValidatorContext);
	}
	
	public List<ValidationMessage> validate(Object requestInstance, String path,JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
        for (JsonRequestValidator v : getJsonRequestValidators()) {
            errors.addAll(v.validate(requestInstance, path,jsonValidatorContext));
        }
        return errors;
	}
	
	public List<JsonRequestValidator> getJsonRequestValidators() {
		return jsonRequestValidators;
	}

	public void setJsonRequestValidators(List<JsonRequestValidator> jsonRequestValidators) {
		this.jsonRequestValidators = jsonRequestValidators;
	}	
	
	private void init(Object object,JsonElementType jsonElementType) {
		if (object instanceof List<?>){
			this.setJsonRequestValidators(JsonValidatorFactory.generate(object,jsonElementType));
		}
		else {
			this.setJsonRequestValidators(JsonValidatorFactory.generate(object,jsonElementType));
		}
	}

	public JsonValidatorContext getJsonValidatorContext() {
		return jsonValidatorContext;
	}

	public void setJsonValidatorContext(JsonValidatorContext jsonValidatorContext) {
		this.jsonValidatorContext = jsonValidatorContext;
	}
	
}
