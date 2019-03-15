package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class PatternValidator extends BaseJsonRequestValidator implements JsonRequestValidator {
	
	private Field field;

	public PatternValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		boolean fieldHasPattern = false;
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		if(!EMPTY_STRING.equals(jsonFieldKeyValidator.pattern())) {
			fieldHasPattern = true;
		}
		boolean isPatternValid = true;
		if(fieldHasPattern) {
			if((jsonFieldKeyValidator.nonNull() && fieldValue==null)  || ((jsonFieldKeyValidator.nonEmpty()||jsonFieldKeyValidator.nonNull()) && EMPTY_STRING.equals(fieldValue))){
				isPatternValid = false;
			}
		}
		isPatternValid = PatternValidator.isValid(jsonFieldKeyValidator.pattern(), fieldValue+"");
		if(!isPatternValid) {
			errors = prepareFieldViolationMessage(ValidatorType.PATTERN,field,errors,path,"pattern error");	
		}
		return errors;
	}
	
	public static boolean isValid(String patternString, String inputString) { 
		Pattern pattern = Pattern.compile(patternString);
		Matcher m = pattern.matcher(inputString);
		return m.matches();
	}
}
