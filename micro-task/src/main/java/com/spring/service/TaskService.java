package com.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dto.TaskRequestDto;
import com.spring.dto.TaskResponseDto;
import com.spring.payload.ApiResponse;

public interface TaskService {

	ApiResponse<TaskResponseDto> saveTask(TaskRequestDto dto);
	
	ApiResponse<List<TaskResponseDto>> getAllTask();
	
	ApiResponse<TaskResponseDto> getTaskById(String id);
	
	ApiResponse<List<TaskResponseDto>> getTaskByProjectId(String projectId);
	
	ApiResponse<List<TaskResponseDto>> getTaskByAssignedUserId(String assignedUserId);
	
	ApiResponse<TaskResponseDto> updateTask(String id , TaskRequestDto dto);
	
	ApiResponse<TaskResponseDto> updateTaskStatus(String id, String status);
	
	ApiResponse<Object> deleteTask(String id);
	
	ApiResponse<Object> deleteTasksByProjectId(String projectId);

	ApiResponse<Object> unassignTasksByUserId(String userId);
}
