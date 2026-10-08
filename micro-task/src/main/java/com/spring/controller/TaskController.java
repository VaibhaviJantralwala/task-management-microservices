package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;

import com.spring.dto.TaskRequestDto;
import com.spring.dto.TaskResponseDto;
import com.spring.payload.ApiResponse;
import com.spring.service.TaskService;

import jakarta.validation.Valid;
import jakarta.ws.rs.Path;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@PostMapping()
	public ResponseEntity<ApiResponse<TaskResponseDto>> saveTask(@Valid @RequestBody TaskRequestDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.saveTask(dto));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getAllTask() {
		return ResponseEntity.ok(taskService.getAllTask());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(@PathVariable String id) {
		return ResponseEntity.ok(taskService.getTaskById(id));
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTaskByAssignedUserId(@PathVariable String id) {
		return ResponseEntity.ok(taskService.getTaskByAssignedUserId(id));
	}
	
	@GetMapping("/project/{id}")
	public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTaskByProjectId(@PathVariable String id) {
		return ResponseEntity.ok(taskService.getTaskByProjectId(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(@Valid @RequestBody TaskRequestDto dto,@PathVariable String id) {
		return ResponseEntity.ok(taskService.updateTask(id, dto));
	}
	
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<TaskResponseDto>> updateTaskStatus(@PathVariable String id ,@Valid @RequestBody TaskRequestDto dto ) {
		return ResponseEntity.ok(taskService.updateTaskStatus(id, dto.getStatus()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteTask(@PathVariable String id) {
		return ResponseEntity.ok(taskService.deleteTask(id));
	}
	
	@DeleteMapping("/project/{projectId}")
	public ResponseEntity<ApiResponse<Object>> deleteTaskByProjectId(@PathVariable String projectId) {
		return ResponseEntity.ok(taskService.deleteTasksByProjectId(projectId));
	}
	
	@PatchMapping("/user/{userId}/unassign")
	public ResponseEntity<ApiResponse<Object>> unassignTasksByUserId(@PathVariable String userId) {
		return ResponseEntity.ok(taskService.unassignTasksByUserId(userId));
	}
	
}
