package com.spring.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.key}")
	private String key;

	@Value("${jwt.expiration}")
	private Long jwtExpiration;
	
	public String generateToken(UserDetails userDetails) {
		
		List<String> roles = new ArrayList<>();
		for( GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.add(authority.getAuthority());
		}
		
		// for claims
		Map<String, Object> map = new HashMap<>();
		map.put("roles", roles);
		
		String token = Jwts
		.builder()
		.claims(map)
		.subject(userDetails.getUsername())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
		.signWith(generateSecretKey(), Jwts.SIG.HS512)
		.compact();
		
		return token;
	}
	
	public SecretKey generateSecretKey() {
//		SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
		return secretKey;
	}
	
	private Claims getPayload(String token) {
		
		Jws<Claims> signedClaims = Jwts.parser()
									   .verifyWith((SecretKey)generateSecretKey())
									   .build()
									   .parseSignedClaims(token);
		return signedClaims.getPayload();
	}
	
	public boolean isExpired(String token) {
		
		Claims payload = getPayload(token);
		Date expiration = payload.getExpiration();
		return expiration.before(new Date());
	}
	
	public String getUsername(String token) {
		
		Claims payload = getPayload(token);
		String username = payload.getSubject();
		return username;
	}
	
	public List<String> getAllRoles(String token){
		
		Claims payload = getPayload(token);
		return (List<String>)payload.get("roles");
	}
}
