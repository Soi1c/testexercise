package com.pestov.testexercise.dto;


import org.json.JSONObject;

public class UserDto {

    private String email;

    private String password;

    public UserDto(final String userJson) {
    	JSONObject result = new JSONObject(userJson);
    	this.email = result.getString("email");
    	this.password = result.getString("password");
	}

	public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


}
