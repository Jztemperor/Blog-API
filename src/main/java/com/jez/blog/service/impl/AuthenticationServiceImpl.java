package com.jez.blog.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.AuthenticationException;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jez.blog.dto.auth.LoginDTO;
import com.jez.blog.dto.auth.LoginResponseDTO;
import com.jez.blog.dto.auth.RegistrationDTO;
import com.jez.blog.dto.auth.RegistrationResponseDTO;
import com.jez.blog.exception.DuplicateEntryException;
import com.jez.blog.model.Role;
import com.jez.blog.model.User;
import com.jez.blog.repository.RoleRepository;
import com.jez.blog.repository.UserRepository;
import com.jez.blog.service.AuthenticationService;
import com.jez.blog.service.TokenService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	
	public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
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

	@Override
	public LoginResponseDTO login(LoginDTO loginDTO) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
			);
			
			String token = tokenService.generateJWT(authentication);
			return new LoginResponseDTO(userRepository.findByUsername(loginDTO.getUsername()).get(), token);
		}catch (AuthenticationException authenticationException) {
			return new LoginResponseDTO(null, "");
		}
		
	}

}
