package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.ValidValues;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class ValidValueValidator extends BaseJsonRequestValidator implements JsonRequestValidator {
	
	private Field field;
	
	public ValidValueValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		ValidValues validValues = field.getAnnotation(ValidValues.class);
		boolean validValueViolation = false;
		List<String> predefinedValueList = null;
		String errorMessage = null;
		if((!validValues.allowNull() && fieldValue==null)  || ((!validValues.allowEmpty()||!validValues.allowNull()) && EMPTY_STRING.equals(fieldValue))){
			validValueViolation = true;
			errorMessage = "value cannot be null or empty";
		}
		else if(validValues.value().length>0) {
			String[] allowedValues = validValues.value();
			predefinedValueList =Arrays.asList(allowedValues);			
		}
		else if(!EMPTY_STRING.equals(validValues.dataSetKey())) {
			String dataSetKey = validValues.dataSetKey();
			if(!jsonValidatorContext.getValidValuesDataSet().isEmpty()){
				if(jsonValidatorContext.getValidValuesDataSet().containsKey(dataSetKey)) {
					predefinedValueList = (List<String>) jsonValidatorContext.getValidValuesDataSet().get(dataSetKey);
				}
			}
		}
		if(!predefinedValueList.contains(fieldValue)){
			validValueViolation = true;
			errorMessage = "current value does not match with any of the valid values "+predefinedValueList;
		}
		
		if(validValueViolation) {
			errors = prepareFieldValidValuesViolationMessage(currentInstance, jsonValidatorContext,ValidatorType.VALIDVALUES,field, errors, path, errorMessage);
		}
		return errors;
	}
	

}
