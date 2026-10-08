package com.spring.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.entity.UserM;
import com.spring.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserM user = userRepo.findByUsername(username)
							   .orElseThrow(()-> new UsernameNotFoundException("No user found!"));
		
		
		return User
				.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities(user.getRole())
				.build();

	}

}
