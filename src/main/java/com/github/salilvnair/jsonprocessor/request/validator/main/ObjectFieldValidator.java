package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.JsonProcessorUtil;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.JsonElementType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class ObjectFieldValidator extends BaseJsonRequestValidator implements JsonRequestValidator {
	
	private Field field;
	private JsonProcessorUtil jsonProcessorUtil;
	
	public ObjectFieldValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		if(fieldValue!=null) {
			jsonValidatorContext.setParent(field);
			this.jsonProcessorUtil = new JsonProcessorUtil(fieldValue,JsonElementType.OBJECT);
			if (jsonProcessorUtil != null) {
				errors.addAll(jsonProcessorUtil.validate(fieldValue, path,jsonValidatorContext));
			}
		}
		return errors;
	}
	
}
