package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidation;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;

public class EmailValidator extends BaseJsonRequestValidator implements JsonKeyValidator {
	
	private Field field;
	
	public EmailValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		JsonKeyValidation jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidation.class);
		boolean isFieldTypeEmail = jsonFieldKeyValidator.email();
		boolean invalidEmail = false;
		if(isFieldTypeEmail) {
			if(jsonFieldKeyValidator.allowNull() && fieldValue==null) {
				return Collections.unmodifiableList(errors);
			}
			else if(jsonFieldKeyValidator.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue))) {
				return Collections.unmodifiableList(errors);
			}
			else if((!jsonFieldKeyValidator.allowNull() && fieldValue==null)  
					|| (!jsonFieldKeyValidator.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue)))){
				invalidEmail = true;
			}
			else if(fieldValue instanceof String) {
				String columnStringValue = (String) fieldValue;
				invalidEmail = !EmailValidator.isValid(columnStringValue);
			}
			if(invalidEmail) {
				errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.EMAIL,field,errors,path,"invalid email error");
			}
			
		}
		return Collections.unmodifiableList(errors);
	}
	
	public static boolean isValid(String emailString) {
		String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
		String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
		String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]"; 
		Pattern pattern = Pattern.compile("^" + ATOM + "+(\\." + ATOM + "+)*@" + DOMAIN + "|" + IP_DOMAIN + ")$", 2);
		if ((emailString == null) || (emailString.length() == 0)) {
			return false;
		}
		Matcher m = pattern.matcher(emailString);
		return m.matches();
	}
}
