package com.jez.blog.service;

import com.jez.blog.dto.auth.LoginDTO;
import com.jez.blog.dto.auth.LoginResponseDTO;
import com.jez.blog.dto.auth.RegistrationDTO;
import com.jez.blog.dto.auth.RegistrationResponseDTO;

public interface AuthenticationService {
	
	public RegistrationResponseDTO register(RegistrationDTO registrationDTO);
	public LoginResponseDTO login(LoginDTO loginDTO);
	
}
