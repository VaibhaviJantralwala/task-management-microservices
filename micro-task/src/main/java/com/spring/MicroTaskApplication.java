package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroTaskApplication.class, args);
	}

}
