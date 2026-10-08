package com.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		
		http
		.csrf(c->c.disable())
		 .formLogin(form -> form.disable())
         .httpBasic(basic -> basic.disable())
		.authorizeExchange(a->a.anyExchange().permitAll());
		
		return http.build();
	}
}
