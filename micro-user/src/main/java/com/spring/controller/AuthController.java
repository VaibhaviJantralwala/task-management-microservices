package com.spring.controller;

import java.util.Optional;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.UserLoginDto;
import com.spring.dto.UserRegisterDto;
import com.spring.dto.UserResponseDto;
import com.spring.entity.UserM;
import com.spring.payload.ApiResponse;
import com.spring.repository.UserRepository;
import com.spring.security.JwtUtil;
import com.spring.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody UserRegisterDto userDto) {
//		UserM user = new UserM();
//		user.setId(UUID.randomUUID().toString());
//		user.setUsername(userDto.getUsername());
//		user.setPassword(encoder.encode(userDto.getPassword()));
//		user.setEmail(userDto.getEmail());
//		user.setDepartment(userDto.getDepartment());
//		user.setMobile(userDto.getMobile());
//		user.setRole("ROLE_"+userDto.getRole().toUpperCase());
//		
//		userRepo.save(user);
		
		ApiResponse<UserResponseDto> response = userService.saveUser(userDto);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody UserLoginDto userDto) {
		
//		UserM userM = userRepo.findByUsername(userDto.getUsername());
//		if( encoder.matches(userDto.getPassword(), userM.getPassword() ) ) {
//			return jwtUtil.generateToken(userM.getUsername());
//		}
//		throw new RuntimeException("Invalid Credentials!");
		
		Authentication auth = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
		
		Authentication getAuth = authManager.authenticate(auth);
		
		UserDetails userDetails = (UserDetails) getAuth.getPrincipal();
		
		String token = jwtUtil.generateToken(userDetails);
		
		return token;
	}
}
