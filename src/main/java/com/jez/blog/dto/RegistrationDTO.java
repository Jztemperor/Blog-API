package com.jez.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
	
	@NotEmpty(message = "Username is required!")
	private String username;
	
	@Email(message = "Email must be in a valid format!")
	@NotEmpty(message = "Email is required!")
	private String email;
	
	@NotEmpty(message = "Password is required!")
	private String password;

}
