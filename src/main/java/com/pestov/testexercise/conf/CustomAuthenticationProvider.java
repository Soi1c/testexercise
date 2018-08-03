package com.pestov.testexercise.conf;

import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

//@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		final CustomUser customUser = userRepository.findByEmail(auth.getName());
		if ((customUser == null)) {
			throw new BadCredentialsException("Invalid username or password");
		}

		final Authentication result = super.authenticate(auth);
		return new UsernamePasswordAuthenticationToken(customUser, result.getCredentials(), result.getAuthorities());
	}

	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (final NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
