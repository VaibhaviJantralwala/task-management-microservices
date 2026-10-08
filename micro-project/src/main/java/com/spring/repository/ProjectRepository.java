package com.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.entity.ProjectM;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectM, String>{

	
}
