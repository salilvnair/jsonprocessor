package com.github.salilvnair.jsonprocessor.request.validator.core;

import java.lang.reflect.Field;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.annotation.UserDefinedMessage;
import com.github.salilvnair.jsonprocessor.request.constant.JsonKeyValidatorConstant;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;

public abstract class BaseJsonRequestValidator {
	
	private JsonValidatorContext jsonValidatorContext;
	
	public static final String EMPTY_STRING = "";

	public JsonValidatorContext getJsonValidatorContext() {
		return jsonValidatorContext;
	}

	public void setJsonValidatorContext(JsonValidatorContext jsonValidatorContext) {
		this.jsonValidatorContext = jsonValidatorContext;
	}
	
	public List<ValidationMessage> prepareFieldViolationMessage(ValidatorType validatorType,Field field,List<ValidationMessage> errors, String path, String systemMsg) {
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		ValidationMessage validationMessage = new ValidationMessage();
		validationMessage.setMessage(systemMsg);
		validationMessage.setType(jsonFieldKeyValidator.messageType());
		String errorMessage = null;
		if(!EMPTY_STRING.equals(jsonFieldKeyValidator.userDefinedMessage())) {
			errorMessage = jsonFieldKeyValidator.userDefinedMessage();
			if(path!=null) {
				errorMessage = errorMessage.replace(JsonKeyValidatorConstant.PATH_PLACEHOLDER, path);
			}
			validationMessage.setMessage(errorMessage);			
		}
		else if(jsonFieldKeyValidator.userDefinedMessages().length>0) {
			for(UserDefinedMessage userDefinedMessageItr : jsonFieldKeyValidator.userDefinedMessages()) {
				if(validatorType.equals(userDefinedMessageItr.validatorType())) {
					errorMessage = userDefinedMessageItr.message();
					if(path!=null) {
						errorMessage = errorMessage.replace(JsonKeyValidatorConstant.PATH_PLACEHOLDER, path);
					}
					validationMessage.setMessage(errorMessage);
					validationMessage.setType(userDefinedMessageItr.messageType());
				}
			}
		}
		validationMessage.setPath(path);		
		errors.add(validationMessage);
		return errors;
	}

}
