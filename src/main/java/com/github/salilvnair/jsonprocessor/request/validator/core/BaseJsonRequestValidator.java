package com.github.salilvnair.jsonprocessor.request.validator.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.annotation.UserDefinedMessage;
import com.github.salilvnair.jsonprocessor.request.annotation.ValidValues;
import com.github.salilvnair.jsonprocessor.request.constant.JsonKeyValidatorConstant;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.PathInfoContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;

public abstract class BaseJsonRequestValidator {
		
	public static final String EMPTY_STRING = "";
	
	public List<ValidationMessage> prepareFieldViolationMessage(Object currentInstance, JsonValidatorContext jsonValidatorContext,ValidatorType validatorType,Field field,List<ValidationMessage> errors, String path, String systemMsg) {
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		ValidationMessage validationMessage = new ValidationMessage();
		validationMessage.setMessage(systemMsg);
		validationMessage.setType(jsonFieldKeyValidator.messageType());
		if(!EMPTY_STRING.equals(jsonFieldKeyValidator.messageId())) {
			validationMessage.setMessageId(jsonFieldKeyValidator.messageId());
		}
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
						if(!EMPTY_STRING.equals(userDefinedMessageItr.messageId())) {
							if(!jsonValidatorContext.getUserDefinedMessageDataSet().isEmpty()) {
								if(jsonValidatorContext.getUserDefinedMessageDataSet().containsKey(userDefinedMessageItr.messageId())) {
									errorMessage = jsonValidatorContext.getUserDefinedMessageDataSet().get(userDefinedMessageItr.messageId());
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
		if(!EMPTY_STRING.equals(validValues.messageId())) {
			validationMessage.setMessageId(validValues.messageId());
		}
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
	
	public String extractUserDefinedMessageIdData(String messageId, JsonValidatorContext jsonValidatorContext) {
		String errorMessage = null;
		if(!EMPTY_STRING.equals(messageId)) {
			if(!jsonValidatorContext.getUserDefinedMessageDataSet().isEmpty()) {
				if(jsonValidatorContext.getUserDefinedMessageDataSet().containsKey(messageId)) {
					errorMessage = jsonValidatorContext.getUserDefinedMessageDataSet().get(messageId);
				}
			}
		}
		return errorMessage;
	}
	
	public String extractCurrentInstanceId(Object currentInstance) {
		if(currentInstance!=null && currentInstance.getClass().isAnnotationPresent(JsonKeyValidator.class)) {
			JsonKeyValidator jsonKeyValidator = currentInstance.getClass().getAnnotation(JsonKeyValidator.class);
			return jsonKeyValidator.id();
		}
		return null;
	}
	
	public void extractFieldPathInfoAndSetInValidatorContext(ValidatorType validatorType, JsonValidatorContext jsonValidatorContext, ValidationMessage message) {
		Map<String, List<PathInfoContext>> fieldPathInfo = jsonValidatorContext.getFieldPathInfo();
		String path = message.getPath();
		List<PathInfoContext> pathInfoList = null;
		if(fieldPathInfo.containsKey(message.getPath())) {
			pathInfoList = fieldPathInfo.get(path);

		}
		else {
			pathInfoList = new ArrayList<>();
		}
		PathInfoContext pathInfoContext = new PathInfoContext();
		pathInfoContext.setMessageType(message.getType());
		pathInfoContext.setValidatorType(validatorType);
		pathInfoList.add(pathInfoContext);
		fieldPathInfo.put(path, pathInfoList);
	}
}
