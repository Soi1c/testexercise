package com.pestov.testexercise.services;

import com.pestov.testexercise.models.User;

public interface IRegTokenService {

	String saveNewRegToken(User user);

	void approveUserAndDeleteToken(String token);
}
