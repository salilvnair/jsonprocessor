package com.github.salilvnair.jsonprocessor.request.helper;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.task.ICustomJsonValidatorTask;

public class JsonValidatorTaskUtil {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String methodName;
	
	private ICustomJsonValidatorTask taskClass;
	
	public  void setTask(ICustomJsonValidatorTask taskClass) {
		this.taskClass = taskClass;
	}
	
	public Object invoke(Object...parameters) {
		if(parameters==null) {
			return null;
		}
		
		Class<?>[] paramString = {};
		if (parameters.length != 0) {
			paramString = new Class[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i] != null) {

					paramString[i] = parameters[i].getClass();
				}
			}
		}
		
		Method method = null;
		try {			
			method = taskClass.getClass().getDeclaredMethod(taskClass.getMethodName(), paramString);

			return method.invoke(taskClass, parameters);
		} 
		
		catch (Exception ex) {
			logger.error("Exception from ExcelValidatorUtil.invoke>>",ex);			
		}
		return null;
	}

	public Object executeTask(ICustomJsonValidatorTask taskClass,JsonValidatorContext validatorContext) {
		this.taskClass = taskClass;
		Object obj = invoke(validatorContext);
		return obj;
	}

	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	

}
