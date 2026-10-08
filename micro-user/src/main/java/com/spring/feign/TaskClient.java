package com.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.payload.ApiResponse;

@FeignClient( name = "micro-task" )
public interface TaskClient {

	@PatchMapping("/task/user/{userId}/unassign")
	ApiResponse<Object> unassignTasksByUserId(@PathVariable String userId);
}
