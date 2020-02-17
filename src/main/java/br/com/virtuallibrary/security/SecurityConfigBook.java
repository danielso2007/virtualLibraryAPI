package br.com.virtuallibrary.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.enums.Roles;

/**
 * Book security. Anyone can read resources. Only admins can modify resources.
 * 
 * @author daniel
 */
@EnableWebSecurity
@Configuration
@Order(value = 2)
public class SecurityConfigBook extends WebSecurityConfigurerAdapter {

	private static final String PATH = Constants.BOOKS;
	
	@Value("${spring.security.user.name}")
	private String user;
	@Value("${spring.security.user.password}")
	private String password;

	@Autowired
	public void configureGlobal1(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		auth
        .inMemoryAuthentication()
        .withUser(user)
        .password(encoder.encode(password))
        .roles(Roles.USER.toString(), Roles.ADMIN.toString());
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.httpBasic().and()
		        .authorizeRequests()
				.antMatchers(HttpMethod.GET, PATH + "/**").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
				.antMatchers(HttpMethod.POST, PATH).hasRole(Roles.ADMIN.toString())
				.antMatchers(HttpMethod.PUT, PATH + "/**").hasRole(Roles.ADMIN.toString())
				.antMatchers(HttpMethod.PATCH, PATH + "/**").hasRole(Roles.ADMIN.toString())
				.antMatchers(HttpMethod.DELETE, PATH + "/**").hasRole(Roles.ADMIN.toString())
				.anyRequest().authenticated();
		httpSecurity.csrf().disable();
		httpSecurity.formLogin().disable();
		httpSecurity.headers().frameOptions().disable();
	}

}