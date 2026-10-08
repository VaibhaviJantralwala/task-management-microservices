package com.spring.security;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.spring.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter{

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		String path = exchange.getRequest().getPath().value();
		
		// login and register should be public 
		if( path.equals("/auth/login") || path.equals("/auth/register")) {
			return chain.filter(exchange);
		}
		
		String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		
		if( header == null || !header.startsWith("Bearer ")) {
			 exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
	         return exchange.getResponse().setComplete();
		}
		
		String token = header.substring(7);
		
		try {
			
			if( jwtUtil.isExpired(token)) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}
			
			// token == valid , forward -> request
			return chain.filter(exchange);
		}
		catch (Exception e) {
			 // Invalid JWT
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
		}
	}	
}
