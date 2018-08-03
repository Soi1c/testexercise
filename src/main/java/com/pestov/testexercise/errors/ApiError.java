package com.pestov.testexercise.errors;

public enum ApiError {

	USER_NOT_FOUND ("0100", "CustomUser not found"),
	WRONG_PASSWORD ("0110", "Wrong passwrod");

	public final String errCode;
	public final String errDesc;

	ApiError(String errCode, String errDesc) {
		this.errCode = errCode;
		this.errDesc = errDesc;
	}
}
