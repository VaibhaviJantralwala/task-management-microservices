package com.spring.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
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
	
	public SecretKey generateSecretKey() {
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
}
