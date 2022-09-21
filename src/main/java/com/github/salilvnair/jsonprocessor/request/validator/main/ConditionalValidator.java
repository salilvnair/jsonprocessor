package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidation;
import com.github.salilvnair.jsonprocessor.request.annotation.UserDefinedMessage;
import com.github.salilvnair.jsonprocessor.request.constant.JsonKeyValidatorConstant;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;

public class ConditionalValidator  extends BaseJsonRequestValidator  implements JsonKeyValidator {
	
	private Field field;
	
	public ConditionalValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		if(currentInstance.getClass().isAnnotationPresent(JsonKeyValidation.class)) {
			JsonKeyValidation jsonObjectKeyValidator = currentInstance.getClass().getAnnotation(JsonKeyValidation.class);
			JsonKeyValidation jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidation.class);
			AbstractCustomJsonValidatorTask validatorTask;
			try {
				validatorTask = jsonObjectKeyValidator.customTaskValidator().newInstance();
				if(validatorTask!=null) {
					if(!EMPTY_STRING.equals(jsonFieldKeyValidator.condition())) {
						jsonValidatorContext.setPath(path);
						jsonValidatorContext.setField(field);
						jsonValidatorContext.setJsonRequest((JsonRequest) currentInstance);
						String errorMessage = CustomMethodValidator.invokeCustomTask(jsonValidatorContext,jsonFieldKeyValidator.condition(),jsonFieldKeyValidator,validatorTask);
						if(errorMessage!=null) {
							ValidationMessage validationMessage = new ValidationMessage();
							validationMessage.setMessage(errorMessage);
							validationMessage.setPath(path);
							validationMessage.setId(extractCurrentInstanceId(currentInstance));
							validationMessage.setType(jsonFieldKeyValidator.messageType());
							if(!EMPTY_STRING.equals(jsonFieldKeyValidator.messageId())) {
								validationMessage.setMessageId(jsonFieldKeyValidator.messageId());
							}
							else if(jsonFieldKeyValidator.userDefinedMessages().length > 0) {
								for(UserDefinedMessage userDefinedMessageItr : jsonFieldKeyValidator.userDefinedMessages()) {
									if(ValidatorType.CONDITIONAL.equals(userDefinedMessageItr.validatorType())) {
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
										if(!EMPTY_STRING.equals(jsonFieldKeyValidator.messageId())) {
											validationMessage.setMessageId(jsonFieldKeyValidator.messageId());
										}
										validationMessage.setMessage(errorMessage);
										validationMessage.setType(userDefinedMessageItr.messageType());
									}
								}
							}
							errors.add(validationMessage);
						}
					}
				}
			} 
			catch (InstantiationException | IllegalAccessException e) {}
		}
		return Collections.unmodifiableList(errors);
	}	
	
}
