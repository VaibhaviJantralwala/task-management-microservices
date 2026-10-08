package com.spring.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.dto.UserRegisterDto;
import com.spring.dto.UserResponseDto;
import com.spring.entity.UserM;
import com.spring.exception.ResourceNotFoundException;
import com.spring.feign.TaskClient;
import com.spring.payload.ApiResponse;
import com.spring.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private TaskClient taskClient;

	@Override
	public ApiResponse<UserResponseDto> saveUser(UserRegisterDto userDto) {
		UserM userEn = mapper.map(userDto, UserM.class);
		userEn.setId(UUID.randomUUID().toString());
		userEn.setPassword(encoder.encode(userDto.getPassword()));
		userEn.setRole("ROLE_"+userDto.getRole().toUpperCase());
		
		UserM savedUser = userRepo.save(userEn);
		return new ApiResponse<UserResponseDto>("Saved User Successfully!","SUCCESS" , mapper.map(savedUser, UserResponseDto.class));
	}

	@Override
	public ApiResponse<List<UserResponseDto>> getAllUser() {
		List<UserResponseDto> allUsers = userRepo
				.findAll()
				.stream()
				.map( u -> mapper.map(u, UserResponseDto.class))
				.toList();
		return new ApiResponse<List<UserResponseDto>>("All Users","SUCCESS", allUsers);
	}

	@Override
	public ApiResponse<UserResponseDto> getUserById(String id) {
		
		UserM user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with ID : "+id));
		
		return new ApiResponse<UserResponseDto>("User Found!","SUCCESS",mapper.map(user, UserResponseDto.class));
	}

	@Override
	public ApiResponse<UserM> updateUser(String id) {
		return null;
	}

	@Override
	@Transactional
	public ApiResponse<Object> deleteUser(String id) {
		
		if( !userRepo.existsById(id)) {
			throw new ResourceNotFoundException("No User with ID : " + id);
		}
		userRepo.deleteById(id);
		taskClient.unassignTasksByUserId(id);
		return new ApiResponse<Object>("User Deleted", "SUCCESS", Collections.emptyMap() );
	}
	
}
