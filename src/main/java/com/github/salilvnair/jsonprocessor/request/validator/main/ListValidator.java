package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.JsonProcessorUtil;
import com.github.salilvnair.jsonprocessor.request.helper.ReflectionUtil;
import com.github.salilvnair.jsonprocessor.request.type.JsonElementType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class ListValidator extends BaseJsonRequestValidator implements JsonRequestValidator {
	
	private JsonProcessorUtil jsonProcessorUtil;
	private Field field;
	private List<?> nodeList;
	public ListValidator(Field field) {
		this.field = field;
	}
	public ListValidator(List<?> nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		List<?> nodeList = null;
		if(this.nodeList!=null) {
			nodeList = this.nodeList;
		}
		else {
			Object fieldValue = ReflectionUtil.getFieldValue(currentInstance, field);
			this.jsonProcessorUtil = new JsonProcessorUtil(field,JsonElementType.LIST_FIELD);
			if (jsonProcessorUtil != null) {
				errors.addAll(jsonProcessorUtil.validate(currentInstance, path, jsonValidatorContext));
			}
			nodeList = (List<?>) fieldValue;
			jsonValidatorContext.setParent(field);
		}
		if(nodeList!=null) {
			int i = 0;
			for (Object n : nodeList) {//for each in list
				this.jsonProcessorUtil = new JsonProcessorUtil(n,JsonElementType.OBJECT);
				if (jsonProcessorUtil != null) {
					errors.addAll(jsonProcessorUtil.validate(n, path + "[" + i + "]",jsonValidatorContext));
				}
				i++;
			}	
		}
		return Collections.unmodifiableList(errors);
	}
}
