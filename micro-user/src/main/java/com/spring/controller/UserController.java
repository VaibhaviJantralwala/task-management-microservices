package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.UserRegisterDto;
import com.spring.dto.UserResponseDto;
import com.spring.entity.UserM;
import com.spring.payload.ApiResponse;
import com.spring.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	// save
	@PostMapping()
	public ResponseEntity<ApiResponse<UserResponseDto>> saveUser(@Valid @RequestBody UserRegisterDto userDto) {
		ApiResponse<UserResponseDto> saveUser = userService.saveUser(userDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
	}
	
	// get all 
	@GetMapping()
	public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
		ApiResponse<List<UserResponseDto>> allUser = userService.getAllUser();
		
		return ResponseEntity.status(HttpStatus.OK).body(allUser);
	}
	
	// get user by ID
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable String id) {
		ApiResponse<UserResponseDto> userById = userService.getUserById(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(userById);
	}
	
	// update user
	@PutMapping()
	public void updateUser() {
		
	}
	
	// delete user
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable String id) {
		ApiResponse<Object> deleteUser = userService.deleteUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(deleteUser);
	}
}
