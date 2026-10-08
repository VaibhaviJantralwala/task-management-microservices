package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.ProjectRequestDto;
import com.spring.dto.ProjectResponseDto;
import com.spring.payload.ApiResponse;
import com.spring.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ProjectService projService;
	
	@PostMapping()
	public ResponseEntity<ApiResponse<ProjectResponseDto>> saveProject(@Valid @RequestBody ProjectRequestDto dto ) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(projService.saveProject(dto));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getAllProject() {
		
		return ResponseEntity.ok(projService.getAllProject());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(@PathVariable String id) {
		
		return ResponseEntity.ok(projService.getProjectById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(@PathVariable String id,@Valid @RequestBody ProjectRequestDto dto) {
		
		return ResponseEntity.ok(projService.updateProject(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteProject(@PathVariable String id) {
		
		return ResponseEntity.ok(projService.deleteProject(id));
	}
}
