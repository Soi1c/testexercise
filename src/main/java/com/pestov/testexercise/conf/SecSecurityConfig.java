package com.pestov.testexercise.conf;

import com.pestov.testexercise.repositories.UserRepository;
import com.pestov.testexercise.services.CustomDetailsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	private CustomDetailsUserService customUserDetailsService;

	@Autowired
	public SecSecurityConfig(CustomDetailsUserService customDetailsUserService) {
		this.customUserDetailsService = customDetailsUserService;
	}

//	@Autowired
//	private UserRepository userRepository;
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authProvider());
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/js/*", "/images/*", "/css/*","/mocks/*",
						"/login*", "/signup/*", "/signup*", "/badToken*").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.addFilter(new JWTAutheticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailsService))
				.formLogin().loginPage("/login.html")
				.defaultSuccessUrl("/static/homepage.html")
				.failureUrl("/login.html?error=true")
				.and()
				.logout().logoutSuccessUrl("/login.html");
	}

//	@Bean
//	public DaoAuthenticationProvider authProvider() {
//		final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
//		authProvider.setUserDetailsService(customUserDetailsService);
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}
