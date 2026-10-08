package com.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestDto {

	@NotBlank(message = "Project name cannot be Empty or Blank!")
	private String name;

	private String description;

	private String managerId;
}
