package com.jez.blog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import javax.security.sasl.AuthenticationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jez.blog.dto.auth.LoginDTO;
import com.jez.blog.dto.auth.LoginResponseDTO;
import com.jez.blog.dto.auth.RegistrationDTO;
import com.jez.blog.dto.auth.RegistrationResponseDTO;
import com.jez.blog.exception.DuplicateEntryException;
import com.jez.blog.model.Role;
import com.jez.blog.model.User;
import com.jez.blog.repository.RoleRepository;
import com.jez.blog.repository.UserRepository;
import com.jez.blog.service.impl.AuthenticationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private TokenService tokenService;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	@Test
	public void register_SuccessfulRegistration_ReturnsRegistartionResponseDTO() {
		
        // Arrange
        RegistrationDTO registrationDTO = new RegistrationDTO("username", "email@example.com", "password");
        when(userRepository.findByUsername(registrationDTO.getUsername())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");

        Role userRole = new Role();
        userRole.setAuthority("USER");
        when(roleRepository.findByAuthority("USER")).thenReturn(java.util.Optional.of(userRole));

        User savedUser = new User(registrationDTO.getUsername(), registrationDTO.getEmail(), "encodedPassword", Set.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegistrationResponseDTO expectedResponse = new RegistrationResponseDTO(savedUser.getUsername(), savedUser.getEmail(), savedUser.getAuthorities());
        when(modelMapper.map(savedUser, RegistrationResponseDTO.class)).thenReturn(expectedResponse);

        // Act
        RegistrationResponseDTO actualResponse = authenticationService.register(registrationDTO);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
	}
	
	@Test
	public void register_UserAlreadyExists_ThrowsDuplicateEntryExceiption() {
		RegistrationDTO registrationDTO = new RegistrationDTO("existingUsername", "email@example.com", "password");
		when(userRepository.findByUsername(registrationDTO.getUsername())).thenReturn(java.util.Optional.of(new User()));
		
		assertThrows(DuplicateEntryException.class, () -> authenticationService.register(registrationDTO));
	}
	
	@Test
	public void login_SuccessfulLogin_ReturnLoginResponseDTO() {
		LoginDTO loginDTO = new LoginDTO("username", "password");
		
		when(authenticationManager.authenticate(any())).thenReturn(null);
        when(tokenService.generateJWT(any())).thenReturn("generatedToken");

        User user = new User("username", "email@example.com", "encodedPassword", Set.of(new Role("USER")));
        when(userRepository.findByUsername(loginDTO.getUsername())).thenReturn(java.util.Optional.of(user));

        LoginResponseDTO expectedResponse = new LoginResponseDTO(user, "generatedToken");

        // Act
        LoginResponseDTO actualResponse = authenticationService.login(loginDTO);

        // Assert
        assertEquals(expectedResponse, actualResponse);
	}
}
