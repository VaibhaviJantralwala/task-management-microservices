package com.spring.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dto.ProjectRequestDto;
import com.spring.dto.ProjectResponseDto;
import com.spring.entity.ProjectM;
import com.spring.exception.ResourceNotFoundException;
import com.spring.feign.TaskClient;
import com.spring.payload.ApiResponse;
import com.spring.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	@Autowired
	private ProjectRepository projRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TaskClient taskClient;

	@Override
	public ApiResponse<ProjectResponseDto> saveProject(ProjectRequestDto dto) {

		ProjectM project = mapper.map(dto, ProjectM.class);
		project.setId(UUID.randomUUID().toString());
		
		ProjectM savedProj = projRepo.save(project);
		
		return new ApiResponse<ProjectResponseDto>("Project saved Successfully!", "SUCCESS", mapper.map(savedProj, ProjectResponseDto.class));
	}

	@Override
	public ApiResponse<List<ProjectResponseDto>> getAllProject() {

		List<ProjectResponseDto> allProj = projRepo.findAll().stream().map(p->mapper.map(p, ProjectResponseDto.class)).toList();
		
		return new ApiResponse<List<ProjectResponseDto>>("All Projects", "SUCCESS",allProj );
	}

	@Override
	public ApiResponse<ProjectResponseDto> getProjectById(String id) {

		ProjectM project = projRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Project with ID : "+id));
		
		return new ApiResponse<ProjectResponseDto>("Project Found!", "SUCCESS", mapper.map(project, ProjectResponseDto.class));
	}

	@Override
	public ApiResponse<ProjectResponseDto> updateProject(String id, ProjectRequestDto dto) {

		ProjectM project = projRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Project with ID : "+id));
		
		project.setName(dto.getName());
		project.setDescription(dto.getDescription());
		project.setManagerId(dto.getManagerId());
		
		ProjectM updatedProj = projRepo.save(project);
		return new ApiResponse<ProjectResponseDto>("Project Updated!", "SUCCESS", mapper.map(updatedProj, ProjectResponseDto.class));
	}

	@Override
	@Transactional
	public ApiResponse<Object> deleteProject(String id) {
		
		if( !projRepo.existsById(id)) {
			throw new ResourceNotFoundException("No Project with ID : "+id);
		}
		
		projRepo.deleteById(id);
		taskClient.deleteTaskByProjectId(id);
		
		return new ApiResponse<Object>("Project Deleted!", "SUCCESS", Collections.emptyMap());
	}
	
}
