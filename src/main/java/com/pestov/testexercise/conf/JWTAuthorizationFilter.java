package com.pestov.testexercise.conf;

import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.services.CustomDetailsUserService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pestov.testexercise.conf.SecurityConstants.HEADER_STRING;
import static com.pestov.testexercise.conf.SecurityConstants.SECRET;
import static com.pestov.testexercise.conf.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final CustomDetailsUserService customDetailsUserService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomDetailsUserService customDetailsUserService) {
		super(authenticationManager);
		this.customDetailsUserService = customDetailsUserService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken usernamePasswordAuth = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuth);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token == null) return null;
		String email = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody().getSubject();
		UserDetails userDetails = customDetailsUserService.loadUserByUsername(email);
		CustomUser customUser = customDetailsUserService.loadUserByEmail(email);

		return email != null ? new UsernamePasswordAuthenticationToken(customUser, null, userDetails.getAuthorities()) : null;
	}
}
