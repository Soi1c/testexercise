package com.pestov.testexercise.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pestov.testexercise.models.CustomUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import static com.pestov.testexercise.conf.SecurityConstants.*;

public class JWTAutheticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAutheticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			CustomUser customUser = new ObjectMapper().readValue(request.getInputStream(), CustomUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customUser.getEmail(), customUser.getPassword()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		LocalDateTime expirationTime = LocalDateTime.now().plus(EXPIRATION_TIME, ChronoUnit.MILLIS);
		String token = Jwts.builder().setSubject(((User)authResult.getPrincipal()).getUsername())
				.setExpiration(Date.from(expirationTime.atZone(TimeZone.getDefault().toZoneId()).toInstant()))
				.signWith(SignatureAlgorithm.ES256, SECRET)
				.compact();
		response.getWriter().write(token);
		response.addHeader(HEADER_STRING, TOKEN_PREFIX.concat(token));
	}
}
