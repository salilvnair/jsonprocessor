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

public class LengthValidator extends BaseJsonRequestValidator implements JsonKeyValidator {
	
	private Field field;
	
	public LengthValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		JsonKeyValidation jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidation.class);
		int minLength = jsonFieldKeyValidator.minLength();
		int maxLength = jsonFieldKeyValidator.maxLength();
		int length = jsonFieldKeyValidator.length();
		boolean minLengthViolated = false;
		boolean maxLengthViolated = false;
		boolean lengthViolated = false;
		Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		if(jsonFieldKeyValidator.allowNull() && fieldValue==null) {
			return Collections.unmodifiableList(errors);
		}
		else if(jsonFieldKeyValidator.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue))) {
			return Collections.unmodifiableList(errors);
		}
		if(minLength!=-1 || maxLength!=-1 || length!=-1) {
			if(minLength>0) {
				if(fieldValue==null || EMPTY_STRING.equals(fieldValue)){
					if((!jsonFieldKeyValidator.allowNull() && fieldValue==null)  
							|| (!jsonFieldKeyValidator.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue)))){
						minLengthViolated = true;
					}
				}
				else if(fieldValue instanceof String) {
					String columnStringValue = (String) fieldValue;
					if(columnStringValue.length()<minLength) {
						minLengthViolated = true;
					}
				}
				else if(fieldValue instanceof Long) {
					Long columnLongValue = (Long) fieldValue;
					if(columnLongValue!=null && columnLongValue.toString().length()<minLength) {
						minLengthViolated = true;
					}
				}
				else {					
					if(fieldValue!=null && fieldValue.toString().length()<minLength) {
						minLengthViolated = true;
					}
				}
			}
			if(maxLength>0) {
				if(fieldValue==null){
					if((!jsonFieldKeyValidator.allowNull() && fieldValue==null)){
						maxLengthViolated = true;
					}
				}
				else if(fieldValue instanceof String) {
					String columnStringValue = (String) fieldValue;
					if(columnStringValue.length()>maxLength) {
						maxLengthViolated = true;
					}
				}
				else if(fieldValue instanceof Long) {
					Long columnLongValue = (Long) fieldValue;
					if(columnLongValue!=null && columnLongValue.toString().length()>maxLength) {
						maxLengthViolated = true;
					}
				}
				else {					
					if(fieldValue!=null && fieldValue.toString().length()>maxLength) {
						maxLengthViolated = true;
					}
				}
			}
			if(length>0) {
				if(fieldValue==null || EMPTY_STRING.equals(fieldValue)){
					if((!jsonFieldKeyValidator.allowNull() && fieldValue==null)  
							|| (!jsonFieldKeyValidator.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue)))){
						lengthViolated = true;
					}
				}
				else if(fieldValue instanceof String) {
					String columnStringValue = (String) fieldValue;
					if(columnStringValue.length()!=length) {
						lengthViolated = true;
					}
				}
				else if(fieldValue instanceof Long) {
					Long columnLongValue = (Long) fieldValue;
					if(columnLongValue!=null && columnLongValue.toString().length()!=length) {
						lengthViolated = true;
					}
				}
				else {					
					if(fieldValue!=null && fieldValue.toString().length()!=length) {
						lengthViolated = true;
					}
				}
			}
			if(minLengthViolated) {
				errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.MINLENGTH,field,errors,path,"min length error");
			}
			if(maxLengthViolated) {
				errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.MAXLENGTH,field,errors,path,"max length error");
			}
			if(lengthViolated) {
				errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.LENGTH,field,errors,path,"length error");
			}
		}
		return Collections.unmodifiableList(errors);
	}
}
