package com.valdir.helpdesk.services.exceptions;

public class ObjectnotfoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjectnotfoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectnotfoundException(String message) {
		super(message);
	}
	

}
