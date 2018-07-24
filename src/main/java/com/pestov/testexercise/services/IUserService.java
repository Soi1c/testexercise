package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.User;

public interface IUserService {

    User registerNewUser(String userDto);
}
