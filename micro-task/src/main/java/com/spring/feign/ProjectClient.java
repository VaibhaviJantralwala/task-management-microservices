package com.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.dto.ProjectDto;
import com.spring.payload.ApiResponse;

@FeignClient(name = "micro-project")
public interface ProjectClient {

	@GetMapping("/project/{id}")
	ApiResponse<ProjectDto> getProjectById(@PathVariable("id") String id);
}
