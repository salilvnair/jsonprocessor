package com.github.salilvnair.jsonprocessor.rest.retry;

public class RetryExecutorException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String exceptionMessage;
	private String retryExecutorMessage;
	
	public RetryExecutorException(Throwable ex, String retryExecutorMessage, String exceptionMessage) {
		super(ex);
		this.setRetryExecutorMessage(retryExecutorMessage);
		this.setExceptionMessage(exceptionMessage);
	}
	
	public RetryExecutorException(String exception) {
		super(exception);
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	public String getRetryExecutorMessage() {
		return retryExecutorMessage;
	}
	public void setRetryExecutorMessage(String retryExecutorMessage) {
		this.retryExecutorMessage = retryExecutorMessage;
	}
}
