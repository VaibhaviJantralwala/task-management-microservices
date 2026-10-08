package com.spring.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dto.TaskRequestDto;
import com.spring.dto.TaskResponseDto;
import com.spring.entity.TaskM;
import com.spring.exception.ResourceNotFoundException;
import com.spring.feign.ProjectClient;
import com.spring.feign.UserClient;
import com.spring.payload.ApiResponse;
import com.spring.repository.TaskRepository;

import feign.FeignException;

@Service
public class TaskServiceImpl implements TaskService{
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ProjectClient projClient;
	
	@Autowired
	private UserClient userClient;

	@Override
	public ApiResponse<TaskResponseDto> saveTask(TaskRequestDto dto) {

		validateProject(dto.getProjectId());
		validateUser(dto.getAssignedUserId());
		
		TaskM task = mapper.map(dto, TaskM.class);
		task.setId(UUID.randomUUID().toString());
		
		if( task.getStatus() == null ) task.setStatus("TODO");
		if( task.getPriority() == null ) task.setPriority("MEDIUM");

		TaskM taskM = taskRepo.save(task);
		return new ApiResponse<>("Task Saved!", "SUCCESS", mapper.map(taskM, TaskResponseDto.class));
	}

	@Override
	public ApiResponse<List<TaskResponseDto>> getAllTask() {
		
		return new ApiResponse<>("All Tasks", "SUCCESS", toDtoList(taskRepo.findAll()));
	}

	@Override
	public ApiResponse<TaskResponseDto> getTaskById(String id) {
		
		TaskM taskM = findOrThrow(id);
		return new ApiResponse<>("Task Found!", "SUCCESS", mapper.map(taskM, TaskResponseDto.class));
	}

	@Override
	public ApiResponse<List<TaskResponseDto>> getTaskByProjectId(String projectId) {
		
		return new ApiResponse<>("All Tasks", "SUCCESS", toDtoList(taskRepo.findByProjectId(projectId)));
	}

	@Override
	public ApiResponse<List<TaskResponseDto>> getTaskByAssignedUserId(String assignedUserId) {
		
		return new ApiResponse<>("All Tasks", "SUCCESS", toDtoList(taskRepo.findByAssignedUserId(assignedUserId)));
	}

	@Override
	public ApiResponse<TaskResponseDto> updateTask(String id, TaskRequestDto dto) {
		
		TaskM taskM = findOrThrow(id);
		
		validateProject(dto.getProjectId());
		validateUser(dto.getAssignedUserId());
		
		taskM.setTaskName(dto.getTaskName());
		taskM.setDescription(dto.getDescription());
		taskM.setAssignedUserId(dto.getAssignedUserId());
		taskM.setProjectId(dto.getProjectId());
		taskM.setDueDate(dto.getDueDate());
		
		if( dto.getStatus() != null ) taskM.setStatus(dto.getStatus());
		if( dto.getPriority() != null ) taskM.setPriority(dto.getPriority());
		
		TaskM updated = taskRepo.save(taskM);
		
		return new ApiResponse<>("Task Updated!", "SUCCESS", mapper.map(updated, TaskResponseDto.class));
	}
	
	@Override
	public ApiResponse<TaskResponseDto> updateTaskStatus(String id, String status) {

		TaskM taskM = findOrThrow(id);
		taskM.setStatus(status);
		TaskM updated = taskRepo.save(taskM);
		return new ApiResponse<>("Task Updated!", "SUCCESS", mapper.map(updated, TaskResponseDto.class));
	}
	
	@Override
	public ApiResponse<Object> deleteTask(String id) {
		
		if( !taskRepo.existsById(id) ) {
			throw new ResourceNotFoundException("Task not found with ID : " + id);
		}
		taskRepo.deleteById(id);
		return new ApiResponse<Object>("Task Deleted!","SUCCESS", Collections.emptyMap());
	}

	@Override
	@Transactional
	public ApiResponse<Object> deleteTasksByProjectId(String projectId) {

		List<TaskM> tasks = taskRepo.findByProjectId(projectId);
		taskRepo.deleteAll(tasks);
		return new ApiResponse<Object>("Project Tasks Deleted!","SUCCESS", Collections.emptyMap());
	}

	@Override
	@Transactional
	public ApiResponse<Object> unassignTasksByUserId(String userId) {
		
		List<TaskM> tasks = taskRepo.findByAssignedUserId(userId);
		tasks.forEach(t->t.setAssignedUserId(null));
		taskRepo.saveAll(tasks);
		return new ApiResponse<Object>("Tasks of Users Unassigned!","SUCCESS", Collections.emptyMap());
	}
	
	// HELPER METHODS
	
	private TaskM findOrThrow(String id) {
		return taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with ID : " + id));
	}
	
	private List<TaskResponseDto> toDtoList(List<TaskM> tasks){
		return tasks.stream().map(t -> mapper.map(t, TaskResponseDto.class)).toList();
	}
	
	private void validateProject(String projectId) {
		try {
			projClient.getProjectById(projectId);
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("Project not found with ID : " + projectId);
		}
	}
	
	private void validateUser(String userId) {
		if (userId == null || userId.isBlank()) {
			return;
		}
		try {
			userClient.getUserById(userId);
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("User not found with ID : " + userId);
		}
	}

	
}
