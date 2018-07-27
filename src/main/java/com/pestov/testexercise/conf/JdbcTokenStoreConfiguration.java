/*
package com.pestov.testexercise.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class JdbcTokenStoreConfiguration {

	private DataSource dataSource;

	@Autowired
	public JdbcTokenStoreConfiguration(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean(name = "jdbcTokenStore")
	public TokenStore tokenStore() {
		return new JdbcTokenStore(this.dataSource);
	}
}
*/
