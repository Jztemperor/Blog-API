package com.jez.blog.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jez.blog.dto.RegistrationDTO;
import com.jez.blog.dto.RegistrationResponseDTO;
import com.jez.blog.exception.DuplicateEntryException;
import com.jez.blog.model.Role;
import com.jez.blog.model.User;
import com.jez.blog.repository.RoleRepository;
import com.jez.blog.repository.UserRepository;
import com.jez.blog.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	
	public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public RegistrationResponseDTO register(RegistrationDTO registrationDTO) {
	
		// Check if user already exists
		if(userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
			throw new DuplicateEntryException("User", "username", registrationDTO.getUsername());
		}
		
		// Encode password
		String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());
		
		// Get default role and assign it to the user
		Role userRole = roleRepository.findByAuthority("USER").get();
		
		Set<Role> authorities = new HashSet<>();
		authorities.add(userRole);
		
		// Save user
		User user = userRepository.save(new User(registrationDTO.getUsername(), registrationDTO.getEmail(), encodedPassword, authorities));
		
		// Map to DTO and return
		return modelMapper.map(user, RegistrationResponseDTO.class);
	}

}
