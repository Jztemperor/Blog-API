package com.jez.blog.dto;

import java.util.Set;

import com.jez.blog.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseDTO {
    private String username;
    private String email;
    private Set<Role> authorities;
}
