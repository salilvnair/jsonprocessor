package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil.DateFormat;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil.DateTimeFormat;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil.DateType;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;

public class DateValidator extends BaseJsonRequestValidator implements JsonKeyValidator {

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
	
	public static boolean dateEquals(Date inputDate, Date targetDate) {
		if(DateParsingUtil.compareDate(targetDate, inputDate)==0) {
			return true;
		}
		return false;
	}
	
	public static boolean dateGt(Date inputDate, Date targetDate) {
		if(DateParsingUtil.compareDate(inputDate, targetDate) > 0) {
			return true;
		}
		return false;
	}
	
	public static boolean dateLt(Date inputDate, Date targetDate) {
		if(DateParsingUtil.compareDate(inputDate, targetDate) < 0) {
			return true;
		}
		return false;
	}
	
	public static boolean dateGte(Date inputDate, Date targetDate) {
		if(DateParsingUtil.compareDate(inputDate, targetDate) >= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean dateLte(Date inputDate, Date targetDate) {
		if(DateParsingUtil.compareDate(inputDate, targetDate) <= 0) {
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
		com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator jsonFieldKeyValidator = field.getAnnotation(com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator.class);
		boolean isFieldTypeDate = false;
		if(jsonFieldKeyValidator.date()||jsonFieldKeyValidator.dateString()||jsonFieldKeyValidator.dateTimeString()) {
			isFieldTypeDate = true;
		}
		boolean invalidDate = false;
		if(isFieldTypeDate) {
			String errorMessage = null;
			if(jsonFieldKeyValidator.allowNull() && columnValue==null) {
				return Collections.unmodifiableList(errors);
			}
			else if(jsonFieldKeyValidator.allowEmpty() && (columnValue==null || EMPTY_STRING.equals(columnValue))) {
				return Collections.unmodifiableList(errors);
			}
			else if((!jsonFieldKeyValidator.allowNull() && columnValue==null)  
					|| (!jsonFieldKeyValidator.allowEmpty() && (columnValue==null || EMPTY_STRING.equals(columnValue)))){
				invalidDate = true;
				if(!jsonFieldKeyValidator.required()) {
					errorMessage = "field cannot be null or empty ";					
				}
			}
			else if(columnValue instanceof Date) {
				Date dateColumnValue = (Date) columnValue;
				if(!EMPTY_STRING.equals(jsonFieldKeyValidator.dateEq())) {
					invalidDate = !DateValidator.isValidSameDate(dateColumnValue, jsonFieldKeyValidator.dateEq());
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
				if(jsonFieldKeyValidator.date()) {
					DateFormat dateFormat = jsonFieldKeyValidator.dateFormat();
					String dateString = (String)columnValue;
					invalidDate = !isValidDate(dateString, dateFormat);
					if(invalidDate) {
						errorMessage = "invalid date format";
					}
					else {
						Date dateColumnValue = DateParsingUtil.parseDate(dateString, dateFormat);
						errorMessage = validateDateStringUsingMetadata(dateColumnValue, jsonFieldKeyValidator, jsonValidatorContext);
						if(errorMessage!=null) {
							invalidDate = true;
						}
					}				
				}
				else if(jsonFieldKeyValidator.dateString() && !DateValidator.isValidDate((String) columnValue,false)) {
					invalidDate = true;
					errorMessage = "invalid date string";
					
				}
				else if(jsonFieldKeyValidator.dateTimeString() && !DateValidator.isValidDate((String) columnValue,true)) {
					invalidDate = true;
					errorMessage = "invalid date time string";
				}				
			}
			if(invalidDate && errorMessage!=null) {
				errors = prepareFieldViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.DATE,field, errors, path, errorMessage);
			}
			if(!invalidDate) {
				convertIntoDesiredDateType(columnValue,currentInstance,jsonFieldKeyValidator);
			}
		}
		return Collections.unmodifiableList(errors);
	}

	private String validateDateStringUsingMetadata(Date dateColumnValue, com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator jsonFieldKeyValidator,
			JsonValidatorContext jsonValidatorContext) {
		String errorMessage = null;
		boolean invalidDate = false;
		if(!EMPTY_STRING.equals(jsonFieldKeyValidator.dateEq())) {
			Date dateValue = null;
			if(DateType.Value.TODAY.equals(jsonFieldKeyValidator.dateEq())) {
				dateValue = new Date();
			}
			if(dateValue!=null) {
				invalidDate = !DateValidator.dateEquals(dateColumnValue, dateValue);
				if(invalidDate) {
					errorMessage = "date not equals error";
				}	
			}
		}
		else if(!EMPTY_STRING.equals(jsonFieldKeyValidator.dateLt())) {
			Date dateValue = null;
			if(DateType.Value.TODAY.equals(jsonFieldKeyValidator.dateLt())) {
				dateValue = new Date();
			}
			if(dateValue!=null) {
				invalidDate = !DateValidator.dateLt(dateColumnValue, dateValue);
				if(invalidDate) {
					errorMessage = "date not less than expected date error";
				}	
			}
		}
		else if(!EMPTY_STRING.equals(jsonFieldKeyValidator.dateGt())) {
			Date dateValue = null;
			if(DateType.Value.TODAY.equals(jsonFieldKeyValidator.dateGt())) {
				dateValue = new Date();
			}
			if(dateValue!=null) {
				invalidDate = !DateValidator.dateGt(dateColumnValue, dateValue);
				if(invalidDate) {
					errorMessage = "date not greater than expected date error";
				}	
			}
		}
		else if(!EMPTY_STRING.equals(jsonFieldKeyValidator.dateLte())) {
			Date dateValue = null;
			if(DateType.Value.TODAY.equals(jsonFieldKeyValidator.dateLte())) {
				dateValue = new Date();
			}
			if(dateValue!=null) {
				invalidDate = !DateValidator.dateLte(dateColumnValue, dateValue);
				if(invalidDate) {
					errorMessage = "date not less than or equals to expected date error";
				}	
			}
		}
		else if(!EMPTY_STRING.equals(jsonFieldKeyValidator.dateGte())) {
			Date dateValue = null;
			if(DateType.Value.TODAY.equals(jsonFieldKeyValidator.dateGte())) {
				dateValue = new Date();
			}
			if(dateValue!=null) {
				invalidDate = !DateValidator.dateGte(dateColumnValue, dateValue);
				if(invalidDate) {
					errorMessage = "date not greater than or equals to expected date error";
				}	
			}
		}
		return errorMessage;
	}

	private void convertIntoDesiredDateType(Object columnValue, Object currentInstance,
			com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator jsonFieldKeyValidator) {
		if(columnValue instanceof String) {
			String jsonDateValue = (String) columnValue;
			 if(jsonFieldKeyValidator.convertIntoDateTimeString()){				 
				Date inputDate = DateParsingUtil.parseDate(jsonDateValue,DateFormat.getAllDateFormats());
				jsonDateValue = DateParsingUtil.getDesiredDateTimeFormat(jsonFieldKeyValidator.dateTimeFormat(), inputDate);
				ReflectionUtil.setFieldValue(currentInstance, field.getName(), jsonDateValue);
			 }
			 else if(jsonFieldKeyValidator.convertIntoDateString()){
				Date inputDate = DateParsingUtil.parseDate(jsonDateValue,DateFormat.getAllDateFormats());
				jsonDateValue = DateParsingUtil.getDesiredDateFormat(jsonFieldKeyValidator.dateFormat(), inputDate);
				ReflectionUtil.setFieldValue(currentInstance, field.getName(), jsonDateValue);
			 }
		}
	}
}
