package com.github.salilvnair.jsonprocessor.request.test.task;

import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.test.bean.School;

public class SchoolCustomTask extends AbstractCustomJsonValidatorTask{

	public String someRandomCondition(JsonValidatorContext jsonValidatorContext) {
		School school = (School) jsonValidatorContext.getJsonRequest();
		System.out.println(school);
		if(!"Kendriya Vidhayala".equals(school.getName())){
			return "What the hell man, you are supposed to be here :P";
		}
		Map<String,Object> validatorMap = jsonValidatorContext.getUserValidatorMap();
		if(!validatorMap.isEmpty()) {
			String val  = (String) validatorMap.get("test");
			return "What the test man, are you "+val;
		}
		return null;
	}
	
	
}
