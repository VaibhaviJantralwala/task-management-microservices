package com.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.dto.UserRegisterDto;
import com.spring.dto.UserResponseDto;
import com.spring.entity.UserM;
import com.spring.payload.ApiResponse;
import com.spring.repository.UserRepository;

public interface UserService {

	ApiResponse<UserResponseDto> saveUser(UserRegisterDto userDto);
	
	ApiResponse<List<UserResponseDto>> getAllUser();
	
	ApiResponse<UserResponseDto> getUserById(String id);
	
	ApiResponse<UserM> updateUser(String id);
	
	ApiResponse<Object> deleteUser(String id);
	
}
