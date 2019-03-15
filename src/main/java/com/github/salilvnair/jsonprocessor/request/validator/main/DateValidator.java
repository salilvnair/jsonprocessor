package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil;
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
		Date maxDate = DateParsingUtil.parseDate(maxDateString, DateParsingUtil.DateFormats.names());
		if(DateParsingUtil.compareDate(maxDate, inputDate)==0 || DateParsingUtil.compareDate(maxDate, inputDate)==1) {
			return true;
		}
		return false;
	}
	
	public static boolean isValidMinDate(Date inputDate, String minDateString) {
		Date maxDate = DateParsingUtil.parseDate(minDateString, DateParsingUtil.DateFormats.names());
		if(DateParsingUtil.compareDate(maxDate, inputDate)==0 || DateParsingUtil.compareDate(maxDate, inputDate)==-1) {
			return true;
		}
		return false;
	}
	
	public static boolean isValidSameDate(Date inputDate, String dateString) {
		Date maxDate = DateParsingUtil.parseDate(dateString, DateParsingUtil.DateFormats.names());
		if(DateParsingUtil.compareDate(maxDate, inputDate)==0) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		Object columnValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(JsonKeyValidator.class);
		boolean isFieldTypeDate = false;
		if(!EMPTY_STRING.equals(jsonFieldKeyValidator.date())
			||!EMPTY_STRING.equals(jsonFieldKeyValidator.minDate())
			||!EMPTY_STRING.equals(jsonFieldKeyValidator.maxDate())
				) {
			isFieldTypeDate = true;
		}
		boolean invalidDate = false;
		if(isFieldTypeDate) {
			if((!jsonFieldKeyValidator.allowNull() && columnValue==null)  || ((!jsonFieldKeyValidator.allowEmpty()||!jsonFieldKeyValidator.allowNull()) && EMPTY_STRING.equals(columnValue))){
				invalidDate = true;
			}
			else if(columnValue instanceof Date) {
				Date dateColumnValue = (Date) columnValue;
				if(!EMPTY_STRING.equals(jsonFieldKeyValidator.date())) {
					invalidDate = !DateValidator.isValidSameDate(dateColumnValue, jsonFieldKeyValidator.date());
					if(invalidDate) {
						errors = prepareFieldViolationMessage(ValidatorType.NA,field, errors, path, "date error");
					}
				}
				if(!EMPTY_STRING.equals(jsonFieldKeyValidator.minDate())) {
					invalidDate = !DateValidator.isValidMinDate(dateColumnValue, jsonFieldKeyValidator.date());
					if(invalidDate) {
						errors = prepareFieldViolationMessage(ValidatorType.NA,field, errors, path, "minDate error");
					}
				}
				if(!EMPTY_STRING.equals(jsonFieldKeyValidator.maxDate())) {
					invalidDate = !DateValidator.isValidMaxDate(dateColumnValue, jsonFieldKeyValidator.date());
					if(invalidDate) {
						errors = prepareFieldViolationMessage(ValidatorType.NA,field, errors, path, "maxDate error");
					}
				}
			}				
		}
		return errors;
	}
}
