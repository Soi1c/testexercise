package com.pestov.testexercise.conf;

import com.pestov.testexercise.services.CustomDetailsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

	private CustomDetailsUserService customUserDetailsService;

	@Autowired
	public SecSecurityConfig(CustomDetailsUserService customDetailsUserService) {
		this.customUserDetailsService = customDetailsUserService;
	}

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
}
