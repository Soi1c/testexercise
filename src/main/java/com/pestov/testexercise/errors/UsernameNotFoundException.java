package com.pestov.testexercise.errors;

public class UsernameNotFoundException extends RuntimeException {

	public UsernameNotFoundException() {
		super("username not found");
	}

	public UsernameNotFoundException(String message) {
		super(message);
	}
}
