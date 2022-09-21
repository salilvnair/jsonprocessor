package com.github.salilvnair.jsonprocessor.request.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil.DateFormat;
import com.github.salilvnair.jsonprocessor.request.helper.DateParsingUtil.DateTimeFormat;
import com.github.salilvnair.jsonprocessor.request.task.AbstractCustomJsonValidatorTask;
import com.github.salilvnair.jsonprocessor.request.type.MessageType;
import com.github.salilvnair.jsonprocessor.request.type.Numeric;
import com.github.salilvnair.jsonprocessor.request.type.Mode;

@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface JsonKeyValidation {
	String id() default "";
	Mode mode() default Mode.STRICT;
	boolean required() default false; 
    int minItems() default 0;
    long maxItems() default -1L;
	boolean conditional() default false;
	String condition() default "";
	int minLength() default -1;
	int maxLength() default -1;
	int length() default -1;
	boolean date() default false;
	boolean dateString() default false;
	boolean dateTimeString() default false;
	boolean convertIntoDateString() default false;
	boolean convertIntoDateTimeString() default false;
	DateFormat dateFormat() default DateFormat.DASH_MM_DD_YYYY;
	DateTimeFormat dateTimeFormat() default DateTimeFormat.SLASH_MM_DD_YYYY_HH_MM;
	String dateEq() default "";
	String dateLt() default "";
	String dateGt() default "";
	String dateLte() default "";
	String dateGte() default "";
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
	String message() default "";
	String messageId() default "";
	/***
	 * Use the <b>UserDefinedMessage</b> annotation to show multiple user defined messages
	 * when the JsonKeyValidator has more than one validation type
	 * */
	UserDefinedMessage[] userDefinedMessages() default {};
	Class<? extends AbstractCustomJsonValidatorTask> customTaskValidator() default AbstractCustomJsonValidatorTask.class;
	MessageType messageType() default MessageType.ERROR;
}
