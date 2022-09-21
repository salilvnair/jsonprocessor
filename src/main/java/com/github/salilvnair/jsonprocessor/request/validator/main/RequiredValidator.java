package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidation;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;


public class RequiredValidator extends BaseJsonRequestValidator implements JsonKeyValidator {
	
	private Field field;
	
	public RequiredValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		JsonKeyValidation jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidation.class);
		boolean requiredFieldViolation = false;
		if(jsonFieldKeyValidator.allowNull() && fieldValue==null) {
			return Collections.unmodifiableList(errors);
		}
		else if(jsonFieldKeyValidator.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue))) {
			return Collections.unmodifiableList(errors);
		}
		else if((!jsonFieldKeyValidator.allowNull() && fieldValue==null)  
				|| (!jsonFieldKeyValidator.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue)))){
			requiredFieldViolation = true;
		}
		if (requiredFieldViolation) {
			errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.REQUIRED,field,errors,path,"required error");
		}
		return Collections.unmodifiableList(errors);
	}

}
