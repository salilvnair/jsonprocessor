package com.github.salilvnair.jsonprocessor.request.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.salilvnair.jsonprocessor.request.type.MessageType;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ValidValues {
	String[] value() default {};
	String dataSetKey() default "";
	boolean allowNull() default false;
	boolean allowEmpty() default false;
	String message() default "";
	String messageId() default "";
	MessageType messageType() default MessageType.ERROR;
}
