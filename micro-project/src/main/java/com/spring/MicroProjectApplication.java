package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroProjectApplication.class, args);
	}

}
