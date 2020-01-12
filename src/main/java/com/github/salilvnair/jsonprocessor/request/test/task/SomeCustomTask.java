package com.github.salilvnair.jsonprocessor.request.test.task;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;

public class SomeCustomTask extends AbstractCustomJsonValidatorTask{

	public String someRandomCondition(JsonValidatorContext jsonValidatorContext) {
		return null;
	}
	
	
}
