package io.jianxun.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/hello").permitAll().anyRequest().authenticated().and().formLogin()
//				.loginPage("/login").permitAll().and().logout().permitAll();
		http.authorizeRequests().anyRequest().permitAll();
	}


}
