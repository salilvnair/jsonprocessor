package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class ConditionalValidator  extends BaseJsonRequestValidator  implements JsonRequestValidator {
	
	private Field field;
	
	public ConditionalValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,JsonValidatorContext jsonValidatorContext) {
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
						jsonValidatorContext.setField(field);
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
	
}
