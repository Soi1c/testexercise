package com.pestov.testexercise.errors;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("username not found");
	}
}
