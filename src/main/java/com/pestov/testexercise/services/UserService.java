package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.User;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
	public User registerNewUser(String userDto) {
		final User user = new User();
		UserDto dto = new UserDto(userDto);
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		userRepository.save(user);
		String token = regTokenService.saveNewRegToken(user);
		emailService.sendSimpleMessage(
				dto.getEmail(),
				"Подтверждение регистрации",
				applicationUrl.concat("/token=").concat(token)
		);
		return user;
	}
}
