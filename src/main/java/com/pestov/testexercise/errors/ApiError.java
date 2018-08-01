package com.pestov.testexercise.errors;

public enum ApiError {

	USER_NOT_FOUND ("0100", "User not found");

	public final String errCode;
	public final String errDesc;

	ApiError(String errCode, String errDesc) {
		this.errCode = errCode;
		this.errDesc = errDesc;
	}
}
