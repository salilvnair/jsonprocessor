package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;



public class AlphaNumericValidator extends BaseJsonRequestValidator implements JsonRequestValidator {

	private Field field;
	
	public AlphaNumericValidator(Field field) {
		this.field = field;
	}
	
	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object columnValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		
		boolean isFieldAlphaNumeric = false;
		if(jsonFieldKeyValidator.alphaNumeric()){
			isFieldAlphaNumeric = true;
		}
		boolean invalidAlphaNumericDetected = false;
		if(isFieldAlphaNumeric) {
			if((!jsonFieldKeyValidator.allowNull() && columnValue==null)  || ((!jsonFieldKeyValidator.allowEmpty()||!jsonFieldKeyValidator.allowNull()) && EMPTY_STRING.equals(columnValue))){
				invalidAlphaNumericDetected = true;
			}
			else if(columnValue instanceof String) {
				String columnStringValue = (String) columnValue;
				String patternString = "[A-Za-z0-9]+";				
				invalidAlphaNumericDetected = !(PatternValidator.isValid(patternString, columnStringValue));
			}	
			if(invalidAlphaNumericDetected) {
				errors = prepareFieldViolationMessage(jsonValidatorContext,ValidatorType.NA, field, errors, path, "invalid alphanumric format");
			}
		}
		return errors;
	
	}
}
