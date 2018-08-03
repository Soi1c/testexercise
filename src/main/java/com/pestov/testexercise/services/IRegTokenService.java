package com.pestov.testexercise.services;

import com.pestov.testexercise.models.CustomUser;

public interface IRegTokenService {

	String saveNewRegToken(CustomUser customUser);

	boolean approveUserAndDeleteToken(String token);
}
