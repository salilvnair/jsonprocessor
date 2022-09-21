package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidation;
import org.apache.commons.lang.math.NumberUtils;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.Numeric;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;



public class NumericValidator extends BaseJsonRequestValidator implements JsonKeyValidator {

	private Field field;
	
	public NumericValidator(Field field) {
		this.field = field;
	}
	
	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object columnValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		JsonKeyValidation jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidation.class);
		
		boolean isFieldNumeric = false;
		if(jsonFieldKeyValidator.numeric()){
			isFieldNumeric = true;
		}
		Numeric typeOfNumeric = jsonFieldKeyValidator.typeOfNumeric();
		boolean isDefaultNumericType = false;
		if(typeOfNumeric.equals(Numeric.DEFAULT)) {
			isDefaultNumericType = true;
		}
		boolean invalidNumericDetected = false;
		if(isFieldNumeric) {
			if(jsonFieldKeyValidator.allowNull() && columnValue==null) {
				return Collections.unmodifiableList(errors);
			}
			else if(jsonFieldKeyValidator.allowEmpty() && (columnValue==null || EMPTY_STRING.equals(columnValue))) {
				return Collections.unmodifiableList(errors);
			}
			else if((!jsonFieldKeyValidator.allowNull() && columnValue==null)  
					|| (!jsonFieldKeyValidator.allowEmpty() && (columnValue==null || EMPTY_STRING.equals(columnValue)))){
				invalidNumericDetected = true;
			}
			else if(columnValue instanceof String) {
				String columnStringValue = (String) columnValue;
				invalidNumericDetected = !(NumberUtils.isNumber(columnStringValue));
			}	
			else if(columnValue instanceof Long) {
				if(!isDefaultNumericType) {
					if(!typeOfNumeric.equals(Numeric.LONG)) {
						invalidNumericDetected = true;
					}
				}
			}
			else if(columnValue instanceof Double) {
				if(!isDefaultNumericType) {
					if(!typeOfNumeric.equals(Numeric.DOUBLE)) {
						invalidNumericDetected = true;
					}
				}
			}
			else if(columnValue instanceof Short) {
				if(!isDefaultNumericType) {
					if(!typeOfNumeric.equals(Numeric.SHORT)) {
						invalidNumericDetected = true;
					}
				}
			}
			else if(columnValue instanceof Integer) {
				if(!isDefaultNumericType) {
					if(!typeOfNumeric.equals(Numeric.INTEGER)) {
						invalidNumericDetected = true;
					}
				}
			}
			else if(columnValue instanceof Float) {
				if(!isDefaultNumericType) {
					if(!typeOfNumeric.equals(Numeric.FLOAT)) {
						invalidNumericDetected = true;
					}
				}
			}
			if(invalidNumericDetected) {
				errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.NUMERIC,field, errors, path, typeOfNumeric.value()+" format invalid error!");
			}
		}
		return Collections.unmodifiableList(errors);
	
	}
}
