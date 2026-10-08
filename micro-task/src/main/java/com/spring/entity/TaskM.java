package com.spring.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskM {

	@Id
	private String id;
	@Column(nullable = false)
	private String taskName;
	@Column(length = 1000)
	private String description;
	@Column(nullable = false)
	private String projectId;
	@Column
	private String assignedUserId;
	@Column
	private String priority;
	@Column
	private String status;
	@Column
	private LocalDate dueDate;
}
