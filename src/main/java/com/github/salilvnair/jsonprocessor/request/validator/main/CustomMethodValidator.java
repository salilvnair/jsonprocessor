package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.constant.JsonKeyValidatorConstant;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.helper.JsonValidatorTaskUtil;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;

public class CustomMethodValidator extends BaseJsonRequestValidator implements JsonKeyValidator {
	
	private Field field;
	
	public CustomMethodValidator(Field field) {
		this.field = field;
	}
	
	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		if(currentInstance.getClass().isAnnotationPresent(com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator.class)) {
			com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator jsonObjectKeyValidator = currentInstance.getClass().getAnnotation(com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator.class);
			com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator.class);
			AbstractCustomJsonValidatorTask validatorTask;
			try {
				validatorTask = jsonObjectKeyValidator.customTaskValidator().newInstance();
				if(validatorTask!=null) {
					if(!EMPTY_STRING.equals(jsonFieldKeyValidator.customTask())) {
						jsonValidatorContext.setPath(path);
						jsonValidatorContext.setField(field);
						jsonValidatorContext.setJsonRequest((JsonRequest) currentInstance);
						String errorMessage = CustomMethodValidator.invokeCustomTask(jsonValidatorContext,jsonFieldKeyValidator.customTask(),jsonFieldKeyValidator,validatorTask);
						if(errorMessage!=null) {
							ValidationMessage validationMessage = new ValidationMessage();
							validationMessage.setMessage(errorMessage);
							if(!EMPTY_STRING.equals(jsonFieldKeyValidator.messageId())) {
								validationMessage.setMessageId(jsonFieldKeyValidator.messageId());
							}
							validationMessage.setPath(path);
							validationMessage.setId(extractCurrentInstanceId(currentInstance));
							validationMessage.setType(jsonFieldKeyValidator.messageType());
							errors.add(validationMessage);
						}
					}
					else {
						if(jsonFieldKeyValidator.customTasks().length>0) {
							jsonValidatorContext.setPath(path);
							jsonValidatorContext.setField(field);
							jsonValidatorContext.setJsonRequest((JsonRequest) currentInstance);
							String errorMessage = null;
							StringBuilder errorMessageBuilder = new StringBuilder("");
							for(String customTask:jsonFieldKeyValidator.customTasks()) {
								errorMessage =  CustomMethodValidator.invokeCustomTask(jsonValidatorContext,customTask,jsonFieldKeyValidator,validatorTask);
								if(errorMessage!=null) {
									errorMessageBuilder.append(errorMessage);
									errorMessageBuilder.append(",");
								}
							}
							if(!EMPTY_STRING.equals(errorMessageBuilder.toString())) {
								errorMessage = errorMessageBuilder.toString().replaceAll(",$", "");
								ValidationMessage validationMessage = new ValidationMessage();
								if(!EMPTY_STRING.equals(jsonFieldKeyValidator.messageId())) {
									validationMessage.setMessageId(jsonFieldKeyValidator.messageId());
								}
								validationMessage.setMessage(errorMessage);
								validationMessage.setPath(path);
								validationMessage.setId(extractCurrentInstanceId(currentInstance));
								validationMessage.setType(jsonFieldKeyValidator.messageType());
								errors.add(validationMessage);
							}
						}
					}
				}
			} 
			catch (InstantiationException | IllegalAccessException e) {}
		}
		return Collections.unmodifiableList(errors);
	}
	
	public static String invokeCustomTask(JsonValidatorContext jsonValidatorContext, String methodName, com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator jsonFieldKeyValidator, AbstractCustomJsonValidatorTask validatorTask) {
		String errorMessage = null;
		validatorTask.setMethodName(methodName);
		JsonValidatorTaskUtil jsonValidatorTaskUtil = new JsonValidatorTaskUtil();
		Object validatedTaskResponse = jsonValidatorTaskUtil.executeTask(validatorTask, jsonValidatorContext);
		boolean hasInvalidData = false;
			
		if(validatedTaskResponse instanceof String){
			errorMessage = (String) validatedTaskResponse;
			if(errorMessage!=null) {
				hasInvalidData = true;
			}
		}
		else if(validatedTaskResponse instanceof Boolean){
			hasInvalidData =  (boolean) validatedTaskResponse;
			if(hasInvalidData) {
				errorMessage = "conditional error!";
			}
		}
		if(hasInvalidData){
			if(!EMPTY_STRING.equals(jsonFieldKeyValidator.message())){					
				errorMessage = jsonFieldKeyValidator.message();
			}
			else if(!EMPTY_STRING.equals(jsonFieldKeyValidator.messageId())) {
				if(!jsonValidatorContext.getUserDefinedMessageDataSet().isEmpty()) {
					if(jsonValidatorContext.getUserDefinedMessageDataSet().containsKey(jsonFieldKeyValidator.messageId())) {
						errorMessage = jsonValidatorContext.getUserDefinedMessageDataSet().get(jsonFieldKeyValidator.messageId());
					}
				}
			}
		}
		if(errorMessage!=null){
			String path = jsonValidatorContext.getPath();
			if(path!=null) {
				errorMessage = errorMessage.replace(JsonKeyValidatorConstant.PATH_PLACEHOLDER, path);
			}
		}
		return errorMessage;
	}
}
