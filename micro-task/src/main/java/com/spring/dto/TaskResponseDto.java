package com.spring.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {

	private String id;
	private String taskName;
	private String description;
	private String projectId;
	private String assignedUserId;
	private String priority;
	private String status;
	private LocalDate dueDate;
}
