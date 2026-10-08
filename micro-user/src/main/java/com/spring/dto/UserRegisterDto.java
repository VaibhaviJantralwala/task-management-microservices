package com.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
	// validations

	@NotBlank(message="Username cannot be Empty or Blank!")
	private String username;
	
	@NotBlank(message = "Password cannot be Empty or Blank!")
	@Size(min = 6, message = "Password must contain at least 6 characters!")
	private String password;
	
	@NotBlank(message = "Email cannot be Empty or Blank!")
    @Email(message = "Please enter a valid email!")
	private String email;
	
	@NotBlank(message = "Mobile number cannot be Empty or Blank!")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Please enter a valid 10-digit mobile number!")
	private String mobile;
	
	@NotBlank(message = "Department cannot be Empty or Blank!")
	private String department;
	
	@NotBlank(message = "Role cannot be Empty or Blank!")
	private String role;
}
