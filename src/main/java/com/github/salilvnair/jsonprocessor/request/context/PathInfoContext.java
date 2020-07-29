package com.github.salilvnair.jsonprocessor.request.context;

import com.github.salilvnair.jsonprocessor.request.type.MessageType;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;

public class PathInfoContext {
	private ValidatorType validatorType;
	private MessageType messageType;
	private ValidationMessage validationMessage;
	public ValidatorType getValidatorType() {
		return validatorType;
	}
	public void setValidatorType(ValidatorType validatorType) {
		this.validatorType = validatorType;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public ValidationMessage getValidationMessage() {
		return validationMessage;
	}
	public void setValidationMessage(ValidationMessage validationMessage) {
		this.validationMessage = validationMessage;
	}

}
