package com.pestov.testexercise.dto;


import org.json.JSONObject;

public class UserDto {

    private String email;

    private String password;

    private String gCaptchaResponse;

    private String xfHeader;

    public UserDto(String userJson) {
    	JSONObject result = new JSONObject(userJson);
    	this.email = result.getString("email");
    	this.password = result.getString("password");
    	this.gCaptchaResponse = result.getString("g-captcha-response");
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getgCaptchaResponse() {
		return gCaptchaResponse;
	}
}
