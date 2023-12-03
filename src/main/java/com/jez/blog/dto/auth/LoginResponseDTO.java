package com.jez.blog.dto.auth;

import com.jez.blog.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
	
	private User user;
	
	private String jwt;
}
