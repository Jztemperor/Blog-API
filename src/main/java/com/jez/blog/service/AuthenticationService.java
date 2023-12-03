package com.jez.blog.service;

import com.jez.blog.dto.RegistrationDTO;
import com.jez.blog.dto.RegistrationResponseDTO;

public interface AuthenticationService {
	
	public RegistrationResponseDTO register(RegistrationDTO registrationDTO);
}
