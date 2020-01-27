package br.com.virtuallibrary.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.enums.Roles;

/**
 * Book security.
 * Anyone can read resources.
 * Only admins can modify resources.
 * @author daniel
 */
@EnableWebSecurity
@Configuration
@Order(value = 2)
public class SecurityConfigBook extends WebSecurityConfigurerAdapter {

	private static final String PATH = Constants.ROOT_URL + Constants.V1 + "/books";

	@Autowired
	public void configureGlobal1(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
				.disable()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, PATH).permitAll()
				.antMatchers(HttpMethod.GET, PATH + "/*").permitAll()
				.antMatchers(HttpMethod.POST, PATH).hasRole(Roles.ADMIN.toString())
				.antMatchers(HttpMethod.PATCH, PATH + "/*").hasRole(Roles.ADMIN.toString())
				.antMatchers(HttpMethod.DELETE, PATH + "/*").hasRole(Roles.ADMIN.toString())
				.anyRequest()
				.authenticated()
				.and()
				.csrf()
				.disable();
	}

}