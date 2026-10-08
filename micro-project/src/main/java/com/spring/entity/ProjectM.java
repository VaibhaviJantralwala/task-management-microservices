package com.spring.entity;

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
public class ProjectM {

	@Id
	private String id;
	@Column( nullable = false)
	private String name;
	@Column( length = 1000)
	private String description;
	@Column
	private String managerId;
}
