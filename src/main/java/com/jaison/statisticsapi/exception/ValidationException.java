package com.jaison.statisticsapi.exception;


public class ValidationException extends RuntimeException {
	private String errorMessage;

	public ValidationException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
