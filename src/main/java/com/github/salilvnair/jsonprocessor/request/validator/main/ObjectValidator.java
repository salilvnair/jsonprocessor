package com.github.salilvnair.jsonprocessor.request.validator.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidation;
import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.AnnotationUtil;
import com.github.salilvnair.jsonprocessor.request.helper.JsonProcessorUtil;
import com.github.salilvnair.jsonprocessor.request.type.JsonElementType;
import com.github.salilvnair.jsonprocessor.request.type.Mode;
import com.github.salilvnair.jsonprocessor.request.validator.core.BaseJsonRequestValidator;
import com.github.salilvnair.jsonprocessor.request.validator.core.JsonKeyValidator;

public class ObjectValidator extends BaseJsonRequestValidator implements JsonKeyValidator {
	
	private Set<Field> properties = new HashSet<Field>();
	private JsonProcessorUtil jsonProcessorUtil;
	
	public ObjectValidator(Object classInstance) {
		properties = AnnotationUtil.getAnnotatedFields(classInstance.getClass(), JsonKeyValidation.class);
	}

	@Override
	public List<ValidationMessage> validate(Object currentInstance, String path,
			JsonValidatorContext jsonValidatorContext) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		for (Field property : properties) {
			Field parent = jsonValidatorContext.getParent();
			JsonKeyValidation fieldLevelJsonKeyValidation = property.getAnnotation(JsonKeyValidation.class);
			if(!Mode.STRICT.equals(jsonValidatorContext.getMode()) &&
				!Mode.STRICT.equals(fieldLevelJsonKeyValidation.mode()) &&
				!fieldLevelJsonKeyValidation.mode().equals(jsonValidatorContext.getMode())) {
					continue;
			}
			this.jsonProcessorUtil = new JsonProcessorUtil(property,JsonElementType.FIELD);
			this.jsonProcessorUtil.setJsonValidatorContext(jsonValidatorContext);
			if(currentInstance!=null && currentInstance.getClass().isAnnotationPresent(JsonKeyValidation.class)) {
				JsonKeyValidation jsonKeyValidation = currentInstance.getClass().getAnnotation(JsonKeyValidation.class);
				jsonValidatorContext.setId(jsonKeyValidation.id());
			}
			errors.addAll(jsonProcessorUtil.validate(currentInstance,  path + "." + property.getName(),jsonValidatorContext));
			jsonValidatorContext.setParent(parent);
		}
		return Collections.unmodifiableList(errors);
	}

}
