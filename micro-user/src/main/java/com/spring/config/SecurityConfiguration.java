package com.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spring.security.JwtAuthFilter;

@Configuration
public class SecurityConfiguration{
	
	@Autowired
	private JwtAuthFilter jwtauthFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		http
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth->auth.requestMatchers("/auth/login", "/auth/register").permitAll()
										 .anyRequest().authenticated())
		.sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.exceptionHandling(e->e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
		.addFilterBefore(jwtauthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authManager(AuthenticationConfiguration ac ) {
		return ac.getAuthenticationManager();
	}
	
}
