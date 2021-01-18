package com.qbrainx_recruitment.config;

import com.qbrainx_recruitment.security.JwtAuthenticationFilter;
import com.qbrainx_recruitment.security.SecurityContext;
import com.qbrainx_recruitment.serviceImpl.CustomUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
		jsr250Enabled = true,
		prePostEnabled = true,
		securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${cannext.web.paths:}")
	private String[] excludePaths;

	private final CustomUserService customUserService;

	public SecurityConfiguration(final CustomUserService customUserService) {
		this.customUserService = customUserService;
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers(excludePaths);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http.httpBasic().disable()
				.formLogin().disable()
				.logout().disable()
				.csrf().disable()
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.anonymous().disable()
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityContext securityContext() {
		return new SecurityContext();
	}

}
