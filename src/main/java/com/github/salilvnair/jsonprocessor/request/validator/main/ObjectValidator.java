package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.AnnotationUtil;
import com.github.salilvnair.jsonprocessor.request.helper.JsonProcessorUtil;
import com.github.salilvnair.jsonprocessor.request.type.JsonElementType;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonRequestValidator;

public class ObjectValidator extends BaseJsonRequestValidator implements JsonRequestValidator{
	
	private Set<Field> properties = new HashSet<Field>();
	private JsonProcessorUtil jsonProcessorUtil;
	
	public ObjectValidator(Object classInstance) {
		properties = AnnotationUtil.getAnnotatedFields(classInstance.getClass(), JsonKeyValidator.class);
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		for (Field property : properties) {
			this.jsonProcessorUtil = new JsonProcessorUtil(property,JsonElementType.FIELD);
			this.jsonProcessorUtil.setJsonValidatorContext(jsonValidatorContext);
			errors.addAll(jsonProcessorUtil.validate(currentInstance,  path + "." + property.getName(),jsonValidatorContext));
		}
		return Collections.unmodifiableList(errors);
	}

}
