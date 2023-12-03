package com.jez.blog.dto;

import java.util.Set;

import com.jez.blog.model.Role;

public class RegistrationResponseDTO {
    private String username;
    private String email;
    private Set<Role> authorities;
}
