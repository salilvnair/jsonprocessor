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
import com.github.salilvnair.jsonprocessor.request.type.Mode;
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
			Field parent = jsonValidatorContext.getParent();
			JsonKeyValidator fieldLevelJsonKeyValidator = property.getAnnotation(JsonKeyValidator.class);
			if(!Mode.STRICT.equals(jsonValidatorContext.getMode()) &&
				!Mode.STRICT.equals(fieldLevelJsonKeyValidator.mode()) && 
				!fieldLevelJsonKeyValidator.mode().equals(jsonValidatorContext.getMode())) {
					continue;
			}
			this.jsonProcessorUtil = new JsonProcessorUtil(property,JsonElementType.FIELD);
			this.jsonProcessorUtil.setJsonValidatorContext(jsonValidatorContext);
			if(currentInstance!=null && currentInstance.getClass().isAnnotationPresent(JsonKeyValidator.class)) {
				JsonKeyValidator jsonKeyValidator = currentInstance.getClass().getAnnotation(JsonKeyValidator.class);
				jsonValidatorContext.setId(jsonKeyValidator.id());
			}
			errors.addAll(jsonProcessorUtil.validate(currentInstance,  path + "." + property.getName(),jsonValidatorContext));
			jsonValidatorContext.setParent(parent);
		}
		return Collections.unmodifiableList(errors);
	}

}
