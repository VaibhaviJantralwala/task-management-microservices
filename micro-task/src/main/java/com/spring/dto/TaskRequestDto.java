package com.spring.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

	@NotBlank(message = "Task name cannot be Empty or Blank!")
	private String taskName;

	private String description;

	@NotBlank(message = "Project id cannot be Empty or Blank!")
	private String projectId;

	private String assignedUserId;

	@Pattern(regexp = "^(LOW|MEDIUM|HIGH)$", message = "Priority must be LOW, MEDIUM or HIGH!")
	private String priority;

	@Pattern(regexp = "^(TODO|IN_PROGRESS|DONE)$", message = "Status must be TODO, IN_PROGRESS or DONE!")
	private String status;

	private LocalDate dueDate;
}
