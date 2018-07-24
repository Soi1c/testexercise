package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.User;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerNewUser(String userDto) {
        final User user = new User();
        UserDto dto = new UserDto(userDto);
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        userRepository.save(user);
        return user;
    }
}
