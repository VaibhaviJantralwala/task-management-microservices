package com.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.entity.UserM;

@Repository
public interface UserRepository extends JpaRepository<UserM, String>{

	Optional<UserM> findByUsername(String username);
}
