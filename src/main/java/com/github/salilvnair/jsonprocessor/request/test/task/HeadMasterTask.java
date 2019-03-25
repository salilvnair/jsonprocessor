package com.github.salilvnair.jsonprocessor.request.test.task;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.test.bean.HeadMaster;

public class HeadMasterTask extends AbstractCustomJsonValidatorTask{

	public void regulate(JsonValidatorContext jsonValidatorContext) {
		HeadMaster headerMaster = (HeadMaster) jsonValidatorContext.getJsonRequest();
		jsonValidatorContext.getUserValidatorMap().put("hm", headerMaster);
	}
	
	
}
