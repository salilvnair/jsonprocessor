package com.github.salilvnair.jsonprocessor.request.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.salilvnair.jsonprocessor.request.type.MessageType;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;

@Retention(RUNTIME)
@Target(METHOD)
public @interface UserDefinedMessage {
	ValidatorType validatorType();
	String message();
	MessageType messageType() default MessageType.ERROR;
}
