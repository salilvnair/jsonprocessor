package com.github.salilvnair.jsonprocessor.request.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.type.MessageType;
import com.github.salilvnair.jsonprocessor.request.type.Numeric;

@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface JsonKeyValidator {
	boolean required() default false; 
    int minItems() default 0;
    long maxItems() default -1L;
	boolean conditional() default false;
	String condition() default "";
	int minLength() default -1;
	int maxLength() default -1;
	int length() default -1;
	String date() default "";
	String minDate() default "";
	String maxDate() default "";
	boolean email() default false;
	boolean allowNull() default false;
	boolean	allowEmpty() default false;
	String pattern() default "";
	boolean alphaNumeric() default false;
	boolean numeric() default false;
	Numeric typeOfNumeric() default Numeric.DEFAULT;
	String customTask() default "";
	String[] customTasks() default {};
	String[] dependentHeaders() default {};
	String dependentHeaderKey() default "";	
	String userDefinedMessage() default "";
	/***
	 * Use the <b>UserDefinedMessage</b> annotation to show multiple user defined messages
	 * when the JsonKeyValidator has more than one validation type
	 * */
	UserDefinedMessage[] userDefinedMessages() default {};
	Class<? extends AbstractCustomJsonValidatorTask> customTaskValidator() default AbstractCustomJsonValidatorTask.class;
	MessageType messageType() default MessageType.ERROR;
}
