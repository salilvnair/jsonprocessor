package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.constant.JsonKeyValidatorConstant;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.helper.JsonValidatorUtil;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class CustomMethodValidator extends BaseJsonRequestValidator implements JsonRequestValidator {
	
	private Field field;
	
	public CustomMethodValidator(Field field) {
		this.field = field;
	}
	
	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		if(currentInstance.getClass().isAnnotationPresent(JsonKeyValidator.class)) {
			JsonKeyValidator jsonObjectKeyValidator = currentInstance.getClass().getAnnotation(JsonKeyValidator.class);
			JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
			AbstractCustomJsonValidatorTask validatorTask;
			try {
				validatorTask = jsonObjectKeyValidator.customTaskValidator().newInstance();
				if(validatorTask!=null) {
					if(!EMPTY_STRING.equals(jsonFieldKeyValidator.condition())) {
						jsonValidatorContext.setPath(path);
						jsonValidatorContext.setJsonRequest((JsonRequest) currentInstance);
						String errorMessage = CustomMethodValidator.invokeCustomTask(jsonValidatorContext,jsonFieldKeyValidator.condition(),jsonFieldKeyValidator,validatorTask);
						if(errorMessage!=null) {
							ValidationMessage validationMessage = new ValidationMessage();
							validationMessage.setMessage(errorMessage);
							validationMessage.setPath(path);
							validationMessage.setType(jsonFieldKeyValidator.messageType());
							errors.add(validationMessage);
						}
					}
				}
			} 
			catch (InstantiationException | IllegalAccessException e) {}
		}
		return Collections.unmodifiableList(errors);
	}
	
	public static String invokeCustomTask(JsonValidatorContext jsonValidatorContext, String methodName,JsonKeyValidator jsonFieldKeyValidator, AbstractCustomJsonValidatorTask validatorTask) {
		String errorMessage = null;
		validatorTask.setMethodName(methodName);
		JsonValidatorUtil jsonValidatorUtil = new JsonValidatorUtil();
		Object validatedTaskResponse = jsonValidatorUtil.executeTask(validatorTask, jsonValidatorContext);
		boolean hasInvalidData = false;
			
		if(validatedTaskResponse instanceof String){
			errorMessage = (String) validatedTaskResponse;
		}
		else if(validatedTaskResponse instanceof Boolean){
			hasInvalidData =  (boolean) validatedTaskResponse;
		}
		if(hasInvalidData){
			if(!EMPTY_STRING.equals(jsonFieldKeyValidator.userDefinedMessage())){					
				errorMessage = jsonFieldKeyValidator.userDefinedMessage();
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
