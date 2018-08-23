package com.pestov.testexercise.errors;

public class BookNotFoundException extends RuntimeException {

	public BookNotFoundException() {
		super("username not found");
	}
}
