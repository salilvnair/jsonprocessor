package com.github.salilvnair.jsonprocessor.request.validator.core;

import java.util.List;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;



public interface JsonRequestValidator {
	
	public static final String ROOT_PATH = "$";
	
	List<ValidationMessage> validate(Object currentInstance, Object parentInstance, String path,JsonValidatorContext jsonValidatorContext);
}
