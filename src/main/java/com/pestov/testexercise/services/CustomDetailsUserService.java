package com.pestov.testexercise.services;

import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomDetailsUserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		CustomUser customUser = loadUserByEmail(email);
		return new User(customUser.getEmail(),customUser.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
	}



	public CustomUser loadUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
