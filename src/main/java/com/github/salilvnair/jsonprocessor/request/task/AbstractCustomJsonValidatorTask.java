package com.github.salilvnair.jsonprocessor.request.task;

public abstract class AbstractCustomJsonValidatorTask implements ICustomJsonValidatorTask{
	private String methodName;

	public String getMethodName() {
		return this.methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
