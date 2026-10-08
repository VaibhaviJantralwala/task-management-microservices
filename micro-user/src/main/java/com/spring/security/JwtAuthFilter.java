package com.spring.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		
		if( header != null && header.startsWith("Bearer ")) {
			
			String token = header.substring(7);
			
			if( !jwtUtil.isExpired(token)) {
				
				String username = jwtUtil.getUsername(token);
				List<String> allRoles = jwtUtil.getAllRoles(token);
				
				List<SimpleGrantedAuthority> listGA = new ArrayList<>();
				for( String role : allRoles) {
					listGA.add(new SimpleGrantedAuthority(role));
				}
				
				Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, null, listGA);
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
