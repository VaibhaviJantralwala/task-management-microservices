package com.spring;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Jwts;

@SpringBootTest
class MicroUserApplicationTests {

	@Test
	void contextLoads() {
	}
	
//	@Test
//	void keyTest() {
//		
//		SecretKey secretKey = Jwts.SIG.HS512.key().build();
//		String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//		System.out.println("----------------"+encodedKey+"-------------------");
//	}

}
