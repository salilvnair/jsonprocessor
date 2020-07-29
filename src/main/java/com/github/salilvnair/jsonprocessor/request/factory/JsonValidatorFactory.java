package com.github.salilvnair.jsonprocessor.request.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.annotation.ValidValues;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.type.JsonElementType;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.AlphaNumericValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.ConditionalValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.CustomMethodValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.DateValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.EmailValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.LengthValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.ListValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.MaxItemsValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.MinItemsValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.NumericValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.ObjectFieldValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.ObjectValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.PatternValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.RequiredValidator;
import com.github.salilvnair.jsonprocessor.request.validator.main.ValidValueValidator;

public class JsonValidatorFactory {
	public static final String EMPTY_STRING = "";

	public static List<JsonRequestValidator> generate(Object classInstance, JsonElementType jsonElementType) {
		List<JsonRequestValidator> validators = new ArrayList<>();
		if(JsonElementType.OBJECT.equals(jsonElementType)) {
			validators = generateJsonObjectValidators(classInstance);
		}
		else if(JsonElementType.LIST.equals(jsonElementType)) {
			validators = generateJsonListValidators(classInstance);
		}
		else if(JsonElementType.LIST_FIELD.equals(jsonElementType)) {
			validators = generateJsonListFieldValidators((Field) classInstance);
		}
		else if(JsonElementType.FIELD.equals(jsonElementType)) {
			validators = generateJsonFieldValidators((Field) classInstance);
		}
		return validators;
	}

	private static List<JsonRequestValidator> generateJsonListValidators(Object classInstance) {
		List<JsonRequestValidator> validators = new ArrayList<>();
		List<?> nodeList = (List<?>) classInstance;
		validators.add(new ListValidator(nodeList));
		return validators;
	}

	private static List<JsonRequestValidator> generateJsonListFieldValidators(Field property) {
		List<JsonRequestValidator> validators = new ArrayList<>();
		JsonKeyValidator jsonKeyValidator = property.getAnnotation(JsonKeyValidator.class);
		if(jsonKeyValidator.minItems()!=0) {
			validators.add(new MinItemsValidator(property));
		}
		if(jsonKeyValidator.maxItems()!=-1) {
			validators.add(new MaxItemsValidator(property));
		}
		return validators;
	}

	private static List<JsonRequestValidator> generateJsonFieldValidators(Field property) {
		List<JsonRequestValidator> validators = new ArrayList<>();
		JsonKeyValidator jsonKeyValidator = property.getAnnotation(JsonKeyValidator.class);
		if(jsonKeyValidator.required()) {
			validators.add(new RequiredValidator(property));
		}
		if(!EMPTY_STRING.equals(jsonKeyValidator.condition()) && jsonKeyValidator.conditional()) {
			validators.add(new ConditionalValidator(property));
		}
		if(property.isAnnotationPresent(ValidValues.class)) {
			ValidValues validValues = property.getAnnotation(ValidValues.class);
			if(validValues.value().length>0 
					|| validValues.range().length>1 
					|| !EMPTY_STRING.equals(validValues.dataSetKey()) 
					|| validValues.conditionalValue().length>0) {
				validators.add(new ValidValueValidator(property));
			}
		}
		//handle fields of type List,ArrayList...etc
		if(List.class.isAssignableFrom(property.getType())) {
			validators.add(new ListValidator(property));
		}
		//handle fields of type User defined object i.e which implements JsonRequest
		if(JsonRequest.class.isAssignableFrom(property.getType())) {
			validators.add(new ObjectFieldValidator(property));
		}
		if(jsonKeyValidator.numeric()) {
			validators.add(new NumericValidator(property));
		}
		if(jsonKeyValidator.alphaNumeric()) {
			validators.add(new AlphaNumericValidator(property));
		}
		if(jsonKeyValidator.email()) {
			validators.add(new EmailValidator(property));
		}
		if(!EMPTY_STRING.equals(jsonKeyValidator.pattern())) {
			validators.add(new PatternValidator(property));
		}
		if(jsonKeyValidator.date()||jsonKeyValidator.dateString()||jsonKeyValidator.dateTimeString()) {
			validators.add(new DateValidator(property));
		}
		if(!EMPTY_STRING.equals(jsonKeyValidator.customTask())) {
			validators.add(new CustomMethodValidator(property));
		}
		if(EMPTY_STRING.equals(jsonKeyValidator.customTask()) && jsonKeyValidator.customTasks().length > 0) {
			validators.add(new CustomMethodValidator(property));
		}
		if(jsonKeyValidator.minLength()!=-1 || jsonKeyValidator.maxLength()!=-1 || jsonKeyValidator.length()!=-1) {
			validators.add(new LengthValidator(property));
		}
		return validators;
	}

	private static List<JsonRequestValidator> generateJsonObjectValidators(Object classInstance) {
		List<JsonRequestValidator> validators = new ArrayList<>();
		validators.add(new ObjectValidator(classInstance));
		return validators;
	}
}
