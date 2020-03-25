package com.tech.developer.application;

public class BusinessException extends Exception {

	public BusinessException(String message, Throwable cause) {
		super(message, cause);		
	}

	public BusinessException(String message) {
		super(message);		
	}

	public BusinessException(Throwable cause) {
		super(cause);		
	}

	
}
