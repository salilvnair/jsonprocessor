package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil.DateFormat;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil.DateTimeFormat;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class DateValidator extends BaseJsonRequestValidator implements JsonRequestValidator {

	private Field field;
	
	public DateValidator(Field field) {
		this.field = field;
	}
	
	public static boolean isValidMaxDate(Date inputDate, String maxDateString) {
		Date maxDate = DateParsingUtil.parseDate(maxDateString, DateParsingUtil.DateFormat.getAllDateFormats());
		if(DateParsingUtil.compareDate(maxDate, inputDate)==0 || DateParsingUtil.compareDate(maxDate, inputDate)==1) {
			return true;
		}
		return false;
	}
	
	public static boolean isValidMinDate(Date inputDate, String minDateString) {
		Date maxDate = DateParsingUtil.parseDate(minDateString, DateParsingUtil.DateFormat.getAllDateFormats());
		if(DateParsingUtil.compareDate(maxDate, inputDate)==0 || DateParsingUtil.compareDate(maxDate, inputDate)==-1) {
			return true;
		}
		return false;
	}
	
	public static boolean isValidSameDate(Date inputDate, String dateString) {
		Date maxDate = DateParsingUtil.parseDate(dateString, DateParsingUtil.DateFormat.getAllDateFormats());
		if(DateParsingUtil.compareDate(maxDate, inputDate)==0) {
			return true;
		}
		return false;
	}
	
	public static boolean isValidDate(String inputDateString,boolean dateTime) {
		if(dateTime) {
			return DateParsingUtil.isDateTime(inputDateString);
		}
		return DateParsingUtil.isDate(inputDateString);
	}
	
	public static boolean isValidDate(String inputDateString, DateFormat dateFormat) {
		return DateParsingUtil.isDate(inputDateString,dateFormat.value());
	}
	
	public static boolean isValidDate(String inputDateString, DateTimeFormat dateTimeFormat) {
		return DateParsingUtil.isDate(inputDateString,dateTimeFormat.value());
	}
	
	
	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object columnValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		boolean isFieldTypeDate = false;
		if(jsonFieldKeyValidator.date()||jsonFieldKeyValidator.dateString()||jsonFieldKeyValidator.dateTimeString()) {
			isFieldTypeDate = true;
		}
		boolean invalidDate = false;
		if(isFieldTypeDate) {
			String errorMessage = null;
			if((!jsonFieldKeyValidator.allowNull() && columnValue==null)  || ((!jsonFieldKeyValidator.allowEmpty()||!jsonFieldKeyValidator.allowNull()) && EMPTY_STRING.equals(columnValue))){
				invalidDate = true;
				if(!jsonFieldKeyValidator.required()) {
					if(jsonFieldKeyValidator.allowEmpty() && columnValue==null) {
						errorMessage = "field cannot be null";
					}
					else {
						errorMessage = "field cannot be null or empty ";
					}
					
				}
			}
			else if(columnValue instanceof Date) {
				Date dateColumnValue = (Date) columnValue;
				if(!EMPTY_STRING.equals(jsonFieldKeyValidator.dateEquals())) {
					invalidDate = !DateValidator.isValidSameDate(dateColumnValue, jsonFieldKeyValidator.dateEquals());
					if(invalidDate) {
						errorMessage = "date not equals error";
					}
				}
				if(!EMPTY_STRING.equals(jsonFieldKeyValidator.minDate())) {
					invalidDate = !DateValidator.isValidMinDate(dateColumnValue, jsonFieldKeyValidator.minDate());
					errorMessage = "minDate error";
				}
				if(!EMPTY_STRING.equals(jsonFieldKeyValidator.maxDate())) {
					invalidDate = !DateValidator.isValidMaxDate(dateColumnValue, jsonFieldKeyValidator.maxDate());
					errorMessage = "maxDate error";
				}
			}	
			else if(columnValue instanceof String) {
				if(jsonFieldKeyValidator.dateString() && !DateValidator.isValidDate((String) columnValue,false)) {
					invalidDate = true;
					errorMessage = "invalid date string";
					
				}
				else if(jsonFieldKeyValidator.dateTimeString() && !DateValidator.isValidDate((String) columnValue,true)) {
					invalidDate = true;
					errorMessage = "invalid date time string";
				}
				if(errorMessage==null) {
					if(jsonFieldKeyValidator.dateString()) {
						invalidDate = !DateValidator.isValidDate((String) columnValue,jsonFieldKeyValidator.dateFormat());
						errorMessage = "invalid date format expected format["+jsonFieldKeyValidator.dateFormat().value()+"]";

					}
					else if(jsonFieldKeyValidator.dateTimeString()) {
						invalidDate = !DateValidator.isValidDate((String) columnValue,jsonFieldKeyValidator.dateTimeFormat());
						errorMessage = "invalid date time format expected format["+jsonFieldKeyValidator.dateTimeFormat().value()+"]";
					}
				}				
			}
			if(invalidDate && errorMessage!=null) {
				errors = prepareFieldViolationMessage(jsonValidatorContext,ValidatorType.DATE,field, errors, path, errorMessage);
			}
		}
		return errors;
	}
}
