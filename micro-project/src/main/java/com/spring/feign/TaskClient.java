package com.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.payload.ApiResponse;

@FeignClient(name = "micro-task")
public interface TaskClient {

	@DeleteMapping("/task/project/{projectId}")
	ApiResponse<Object> deleteTaskByProjectId(@PathVariable String projectId);
}
