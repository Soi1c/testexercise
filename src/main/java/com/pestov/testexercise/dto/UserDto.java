package com.pestov.testexercise.dto;


import org.json.JSONObject;

public class UserDto {

    private String email;
    private String password;
    private Long id;

	public UserDto(String email, Long id) {
		this.email = email;
		this.id = id;
	}

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

	public Long getId() {
		return id;
	}
}
