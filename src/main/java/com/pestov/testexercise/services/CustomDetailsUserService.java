package com.pestov.testexercise.services;

import com.pestov.testexercise.errors.UsernameNotFoundException;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class CustomDetailsUserService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomDetailsUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		Optional<CustomUser> customUserOptional = userRepository.findByEmail(email);
		if (!customUserOptional.isPresent()) {
			throw new UsernameNotFoundException();
		}
		CustomUser customUser = customUserOptional.get();
		return User.withUsername(customUser.getEmail())
				.password(customUser.getPassword())
				.authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
				.build();
	}

	public CustomUser loadUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(UsernameNotFoundException::new);
	}
}
