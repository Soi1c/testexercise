package com.pestov.testexercise.services;

import com.pestov.testexercise.Dto.UserDto;
import com.pestov.testexercise.models.User;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerNewUser(UserDto userDto) {
        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        return user;
    }
}
