package com.github.salilvnair.jsonprocessor.request.validator.core;

import java.lang.reflect.Field;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.annotation.UserDefinedMessage;
import com.github.salilvnair.jsonprocessor.request.annotation.ValidValues;
import com.github.salilvnair.jsonprocessor.request.constant.JsonKeyValidatorConstant;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;

public abstract class BaseJsonRequestValidator {
		
	public static final String EMPTY_STRING = "";
	
	public List<ValidationMessage> prepareFieldViolationMessage(Object currentInstance, JsonValidatorContext jsonValidatorContext,ValidatorType validatorType,Field field,List<ValidationMessage> errors, String path, String systemMsg) {
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		ValidationMessage validationMessage = new ValidationMessage();
		validationMessage.setMessage(systemMsg);
		validationMessage.setType(jsonFieldKeyValidator.messageType());
		String errorMessage = null;
		if(!EMPTY_STRING.equals(jsonFieldKeyValidator.message())) {
			errorMessage = jsonFieldKeyValidator.message();
			if(path!=null) {
				errorMessage = errorMessage.replace(JsonKeyValidatorConstant.PATH_PLACEHOLDER, path);
			}
			validationMessage.setMessage(errorMessage);			
		}
		else if(!EMPTY_STRING.equals(jsonFieldKeyValidator.messageId())) {
			if(!jsonValidatorContext.getUserDefinedMessageDataSet().isEmpty()) {
				if(jsonValidatorContext.getUserDefinedMessageDataSet().containsKey(jsonFieldKeyValidator.messageId())) {
					errorMessage = jsonValidatorContext.getUserDefinedMessageDataSet().get(jsonFieldKeyValidator.messageId());
				}
			}
		}
		else if(jsonFieldKeyValidator.userDefinedMessages().length>0) {
			for(UserDefinedMessage userDefinedMessageItr : jsonFieldKeyValidator.userDefinedMessages()) {
				if(validatorType.equals(userDefinedMessageItr.validatorType())) {
					errorMessage = userDefinedMessageItr.message();
					if(EMPTY_STRING.equals(errorMessage)) {
						errorMessage = userDefinedMessageItr.validatorType().name()+" error";
						if(!EMPTY_STRING.equals(userDefinedMessageItr.id())) {
							if(!jsonValidatorContext.getUserDefinedMessageDataSet().isEmpty()) {
								if(jsonValidatorContext.getUserDefinedMessageDataSet().containsKey(userDefinedMessageItr.id())) {
									errorMessage = jsonValidatorContext.getUserDefinedMessageDataSet().get(userDefinedMessageItr.id());
								}
							}
						}
					}
					if(path!=null) {
						errorMessage = errorMessage.replace(JsonKeyValidatorConstant.PATH_PLACEHOLDER, path);
					}
					validationMessage.setMessage(errorMessage);
					validationMessage.setType(userDefinedMessageItr.messageType());
				}
			}
		}
		validationMessage.setPath(path);
		validationMessage.setId(extractCurrentInstanceId(currentInstance));
		errors.add(validationMessage);
		return errors;
	}
	
	public List<ValidationMessage> prepareFieldValidValuesViolationMessage(Object currentInstance, JsonValidatorContext jsonValidatorContext,ValidatorType validatorType,Field field,List<ValidationMessage> errors, String path, String systemMsg) {
		ValidValues validValues = field.getAnnotation(ValidValues.class);
		String errorMessage = null;
		ValidationMessage validationMessage = new ValidationMessage();
		validationMessage.setMessage(systemMsg);
		validationMessage.setType(validValues.messageType());
		if(!EMPTY_STRING.equals(validValues.message())) {
			errorMessage = validValues.message();
			if(EMPTY_STRING.equals(errorMessage)) {
				errorMessage = validatorType.name()+" error";
				if(!EMPTY_STRING.equals(validValues.messageId())) {
					if(!jsonValidatorContext.getUserDefinedMessageDataSet().isEmpty()) {
						if(jsonValidatorContext.getUserDefinedMessageDataSet().containsKey(validValues.messageId())) {
							errorMessage = jsonValidatorContext.getUserDefinedMessageDataSet().get(validValues.messageId());
						}
					}
				}
			}
			if(path!=null) {
				errorMessage = errorMessage.replace(JsonKeyValidatorConstant.PATH_PLACEHOLDER, path);
			}
			validationMessage.setMessage(errorMessage);			
		}
		validationMessage.setPath(path);
		validationMessage.setId(extractCurrentInstanceId(currentInstance));
		errors.add(validationMessage);
		return errors;
	}
	
	public String extractCurrentInstanceId(Object currentInstance) {
		if(currentInstance!=null && currentInstance.getClass().isAnnotationPresent(JsonKeyValidator.class)) {
			JsonKeyValidator jsonKeyValidator = currentInstance.getClass().getAnnotation(JsonKeyValidator.class);
			return jsonKeyValidator.id();
		}
		return null;
	}

}
