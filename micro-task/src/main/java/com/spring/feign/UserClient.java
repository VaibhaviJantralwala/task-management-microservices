package com.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.dto.UserDto;
import com.spring.payload.ApiResponse;

@FeignClient( name = "micro-user")
public interface UserClient {

	@GetMapping("/user/{id}")
	ApiResponse<UserDto> getUserById(@PathVariable("id") String id);
}
