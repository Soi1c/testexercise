package com.pestov.testexercise.conf;

import com.pestov.testexercise.services.CustomDetailsUserService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomDetailsUserService customUserDetailsService;

	public SecSecurityConfig(CustomDetailsUserService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
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
				.and()
				.logout().logoutSuccessUrl("/login.html");
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
