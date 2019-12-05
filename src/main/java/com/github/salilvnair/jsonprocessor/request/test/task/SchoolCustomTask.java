package com.github.salilvnair.jsonprocessor.request.test.task;

import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.test.bean.School;

public class SchoolCustomTask extends AbstractCustomJsonValidatorTask{

	public String someRandomCondition(JsonValidatorContext jsonValidatorContext) {
		School school = (School) jsonValidatorContext.getJsonRequest();
		Map<String,Object> validatorMap = jsonValidatorContext.getUserValidatorMap();
		if(!validatorMap.isEmpty() && school.getName()!=null) {
			String alumini  = (String) validatorMap.get("alumini");
			if(!school.getName().contains(alumini)){
				return "Only "+alumini+" alumini schools are allowed";
			}
		}
		return null;
	}
	
	
}
