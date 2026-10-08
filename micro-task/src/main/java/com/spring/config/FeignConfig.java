package com.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor authForwardInterceptor() {
		return template->{
			
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		
			if( attrs != null ) {
				String authHeader = attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
				
				if( authHeader!= null) {
					template.header(HttpHeaders.AUTHORIZATION, authHeader);
				}
			}
		};
	}
}
