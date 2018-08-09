package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IRegTokenService regTokenService;

	@Autowired
	private IEmailService emailService;

	@Value("${spring.application.url}")
	private String applicationUrl;

	@Override
	@Transactional
	public CustomUser registerNewUser(String userDto) {
		final CustomUser customUser = new CustomUser();
		UserDto dto = new UserDto(userDto);
		customUser.setEmail(dto.getEmail());
		customUser.setPassword(dto.getPassword());
		userRepository.save(customUser);
		String token = regTokenService.saveNewRegToken(customUser);
		emailService.sendSimpleMessage(
				dto.getEmail(),
				"Подтверждение регистрации",
				applicationUrl.concat("/signup/confirmEmail?token=").concat(token)
		);
		return customUser;
	}

	public List<CustomUser> getUsers() {
		return userRepository.findAll();
	}
}
