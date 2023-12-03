package com.jez.blog.model;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements GrantedAuthority {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long roleId;
	
	private String authority;
	
	// Constructor for DB seeding
	public Role(String authority) {
		this.authority = authority;
	}
}
