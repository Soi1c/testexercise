package com.pestov.testexercise.errors;

public enum ApiError {

	USER_NOT_FOUND ("0100", "CustomUser not found"),
	WRONG_PASSWORD ("0110", "Wrong password"),

	WRONG_EXTENSION ("0200", "File extension should be TXT");

	public final String errCode;
	public final String errDesc;

	ApiError(String errCode, String errDesc) {
		this.errCode = errCode;
		this.errDesc = errDesc;
	}

	@Override
	public String toString() {
		return "{\"".concat(this.errCode).concat("\":\"").concat(this.errDesc).concat("\"}");
	}
}
