package com.jez.blog.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jez.blog.model.Role;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTest {
	
	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void RoleRepository_Save_ReturnSavedRole() {
		
		Role role = Role.builder().authority("test_role").build();
	
		Role savedRole = roleRepository.save(role);
		
		Assertions.assertThat(savedRole).isNotNull();
		Assertions.assertThat(savedRole)
			.usingRecursiveComparison()
			.ignoringFields("roleId")
			.isEqualTo(role);
}
	
	@Test
	public void RoleRepository_FindByAuthority_ReturnsRole() {
		Role role = Role.builder().authority("test_role").build();
		Role savedRole = roleRepository.save(role);
		
		Optional<Role> foundRoleOptional = roleRepository.findByAuthority("test_role");
		
		Assertions.assertThat(foundRoleOptional).isPresent();
		
		Role foundRole = foundRoleOptional.get();
		Assertions.assertThat(foundRole)
		.usingRecursiveComparison()
		.ignoringFields("roleId")
		.isEqualTo(role);
}
}
