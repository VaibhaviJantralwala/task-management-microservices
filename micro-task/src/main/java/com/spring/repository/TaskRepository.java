package com.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.entity.TaskM;

@Repository
public interface TaskRepository extends JpaRepository<TaskM, String>{

	List<TaskM> findByProjectId(String projectId);
	
	List<TaskM> findByAssignedUserId(String assignedUserId);
}
