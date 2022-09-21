package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidation;
import org.apache.commons.collections4.CollectionUtils;

import com.github.salilvnair.jsonprocessor.request.annotation.ConditionalValidValues;
import com.github.salilvnair.jsonprocessor.request.annotation.ValidValues;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.helper.JsonValidatorTaskUtil;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;

public class ValidValueValidator extends BaseJsonRequestValidator implements JsonKeyValidator {
	
	public static final String VALID_VALUE_VIOLATION_MESSAGE = "current value does not match with any of the valid values";
	
	private Field field;
	
	public ValidValueValidator(Field field) {
		this.field = field;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		StringBuilder errorMsgBuilder = new StringBuilder();
		Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field.getName());
		ValidValues validValues = field.getAnnotation(ValidValues.class);
		JsonKeyValidation jsonObjectKeyValidator = currentInstance.getClass().getAnnotation(JsonKeyValidation.class);
		boolean validValueViolation = false;
		
		String errorMessage = null;
		
		if(validValues.allowNull() && fieldValue==null) {
			return Collections.unmodifiableList(errors);
		}
		else if(validValues.allowEmpty() && (fieldValue==null || EMPTY_STRING.equals(fieldValue))) {
			return Collections.unmodifiableList(errors);
		}
		if(validValues.conditionalValue().length > 0 ) {
			ConditionalValidValues[] conditionalValues = validValues.conditionalValue();
			List<ConditionalValidValues> conditionalValidValuesList = Arrays.stream(conditionalValues).collect(Collectors.toList());
			
			for(ConditionalValidValues conditionalValidValues: conditionalValidValuesList) {
				boolean violatesValidValueCondition = satisfiesValidValueCondition(conditionalValidValues.condition(), jsonObjectKeyValidator, jsonValidatorContext, path, currentInstance);
				if(violatesValidValueCondition) {
					validValueViolation = violatesValidValues(errorMsgBuilder, 
							validValues.showPredefinedValuesInMessage() || conditionalValidValues.showPredefinedValuesInMessage(), 
							conditionalValidValues.value(), 
							conditionalValidValues.dataSetKey(), 
							conditionalValidValues.range(),
							jsonValidatorContext, fieldValue+"");
					if(!EMPTY_STRING.equals(conditionalValidValues.message())) {
						errorMsgBuilder.setLength(0);
						errorMsgBuilder.append(conditionalValidValues.message());
					}
					else if(!EMPTY_STRING.equals(conditionalValidValues.messageId())) {
						String messageIdError = extractUserDefinedMessageIdData(conditionalValidValues.messageId(), jsonValidatorContext);
						if(messageIdError!=null) {
							errorMsgBuilder.setLength(0);
							errorMsgBuilder.append(messageIdError);	
						}
					}
					break;
				}
			}
			
		}
		
		else {
			if(validValues.conditional() && !EMPTY_STRING.equals(validValues.condition())) {
				boolean  satisfiesValidValueCondition = satisfiesValidValueCondition(validValues.condition(), jsonObjectKeyValidator, jsonValidatorContext, path, currentInstance);
				if(satisfiesValidValueCondition) {
					validValueViolation = violatesValidValues(errorMsgBuilder, validValues.showPredefinedValuesInMessage(), validValues.value(), 
							validValues.dataSetKey(), validValues.range(), jsonValidatorContext, fieldValue+"");		
				}
			}
			else {
				validValueViolation = violatesValidValues(errorMsgBuilder, validValues.showPredefinedValuesInMessage(), validValues.value(), 
						validValues.dataSetKey(), validValues.range(), jsonValidatorContext, fieldValue+"");
			}
		}
		
		if(validValueViolation) {
			errorMessage = errorMsgBuilder.toString();
			errors = prepareFieldValidValuesViolationMessage(currentInstance,jsonValidatorContext,ValidatorType.VALIDVALUES,field, errors, path, errorMessage);
		}
		return Collections.unmodifiableList(errors);
	}
	
	public boolean violatesValidValues(StringBuilder errorMsgBuilder,boolean showPredefinedValuesInMessage, 
			String[] allowedValues, String dataSetKey, int[] range, JsonValidatorContext jsonValidatorContext, String fieldValue) {
		List<String> predefinedValueList = null;
		if(allowedValues.length > 0) {
			predefinedValueList = new ArrayList<>(Arrays.asList(allowedValues));			
		}
		if(!EMPTY_STRING.equals(dataSetKey)) {
			if(!jsonValidatorContext.getValidValuesDataSet().isEmpty()){
				if(jsonValidatorContext.getValidValuesDataSet().containsKey(dataSetKey)) {
					List<String> validValueDataSet = jsonValidatorContext.getValidValuesDataSet().get(dataSetKey);
					if(predefinedValueList == null) {
						predefinedValueList = validValueDataSet;
					}
					else {
						predefinedValueList.addAll(validValueDataSet);
					}					
				}
			}
		}
		if(range.length > 1) {
			List<String> rangeList = IntStream
										.range(range[0], range[1])
										.boxed()
										.map(i -> i + "")
										.collect(Collectors.toList());
			if(predefinedValueList == null) {
				predefinedValueList = rangeList;
			}
			else {
				predefinedValueList.addAll(rangeList);
			}	
		}
		if(CollectionUtils.isNotEmpty(predefinedValueList) && !predefinedValueList.contains(fieldValue)){
			errorMsgBuilder.append(VALID_VALUE_VIOLATION_MESSAGE);
			if(showPredefinedValuesInMessage) {
				errorMsgBuilder.append(" "+ predefinedValueList);
			}
			else {
				errorMsgBuilder.append("!!");
			}
			return true;
		}
		return false;
	}
	
	
	public boolean satisfiesValidValueCondition(String condition, JsonKeyValidation jsonObjectKeyValidator, JsonValidatorContext jsonValidatorContext, String path, Object currentInstance) {
		AbstractCustomJsonValidatorTask validatorTask;
		try {
			validatorTask = jsonObjectKeyValidator.customTaskValidator().newInstance();
			if(validatorTask!=null) {
				jsonValidatorContext.setPath(path);
				jsonValidatorContext.setField(field);
				jsonValidatorContext.setJsonRequest((JsonRequest) currentInstance);
				validatorTask.setMethodName(condition);
				JsonValidatorTaskUtil jsonValidatorTaskUtil = new JsonValidatorTaskUtil();
				return (boolean) jsonValidatorTaskUtil.executeTask(validatorTask, jsonValidatorContext);
			}
		}
		catch (InstantiationException | IllegalAccessException e) {}
		return false;
	}
	

}
