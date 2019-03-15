package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class MaxItemsValidator extends BaseJsonRequestValidator implements JsonRequestValidator {
	
	private Field field;
	
	public MaxItemsValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object listObject = ReflectionUtil.getFieldValue(currentInstance, field);
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		List<?> objectList = (List<?>) listObject;
		if(jsonFieldKeyValidator.maxItems()!=-1) {
			if(objectList!=null && objectList.size()>jsonFieldKeyValidator.maxItems()) {				
				errors = prepareFieldViolationMessage(ValidatorType.MAXITEMS,field,errors,path,"max items error");
			}
		}
		return Collections.unmodifiableList(errors);
	}

}
