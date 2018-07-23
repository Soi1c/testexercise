package com.pestov.testexercise.services;

import com.pestov.testexercise.Dto.UserDto;
import com.pestov.testexercise.models.User;

public interface IUserService {

    User registerNewUser(UserDto userDto);
}
