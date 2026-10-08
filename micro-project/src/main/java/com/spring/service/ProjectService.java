package com.spring.service;

import java.util.List;

import java.util.List;

import com.spring.dto.ProjectRequestDto;
import com.spring.dto.ProjectResponseDto;
import com.spring.entity.ProjectM;
import com.spring.payload.ApiResponse;

public interface ProjectService {

	ApiResponse<ProjectResponseDto> saveProject(ProjectRequestDto dto);
	
	ApiResponse<List<ProjectResponseDto>> getAllProject();
	
	ApiResponse<ProjectResponseDto> getProjectById(String id);
	
	ApiResponse<ProjectResponseDto> updateProject(String id,ProjectRequestDto dto);
	
	ApiResponse<Object> deleteProject(String id);

}
