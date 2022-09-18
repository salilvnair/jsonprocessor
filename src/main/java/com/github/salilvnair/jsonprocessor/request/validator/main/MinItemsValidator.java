package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;

public class MinItemsValidator extends BaseJsonRequestValidator implements JsonKeyValidator {
	
	private Field field;
	
	public MinItemsValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object listObject = ReflectionUtil.getFieldValue(currentInstance, field);
		List<?> objectList = (List<?>) listObject;
		com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator.class);
		if(jsonFieldKeyValidator.minItems()!=0) {
			if(jsonFieldKeyValidator.allowNull() && objectList==null) {
				return Collections.unmodifiableList(errors);
			}
			if(jsonFieldKeyValidator.allowEmpty() && (objectList==null || (objectList!=null && objectList.isEmpty()))) {
				return Collections.unmodifiableList(errors);
			}
			if(objectList==null || objectList.size()<jsonFieldKeyValidator.minItems()) {
				errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.MINITEMS,field,errors,path,"min items error");
			}
		}
		return Collections.unmodifiableList(errors);
	}

}
