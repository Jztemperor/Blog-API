package com.jez.blog.repository;

import java.util.HashSet;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jez.blog.model.Role;
import com.jez.blog.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void UserRepository_Save_ReturnsSavedUser() {
		User user = User.builder()
				.username("test_user")
				.email("testmail@domain.com")
				.password("test_pwd")
				.authorities(new HashSet<Role>())
				.build();
		
		user.getAuthorities().add(new Role("user"));
		
		User savedUser = userRepository.save(user);
		
		Assertions.assertThat(savedUser).isNotNull();
		Assertions.assertThat(savedUser)
		.usingRecursiveComparison()
		.ignoringFields("userId")
		.isEqualTo(user);
	}
	
	@Test
	public void UserRepository_FindByUsername_ReturnsUser() {
		User user = User.builder()
				.username("test_user")
				.email("testmail@domain.com")
				.password("test_pwd")
				.authorities(new HashSet<Role>())
				.build();
		
		user.getAuthorities().add(new Role("user"));
		userRepository.save(user);
		
		Optional<User> foundUserOptional = userRepository.findByUsername(user.getUsername());
		
		Assertions.assertThat(foundUserOptional).isPresent();
		
		User foundUser = foundUserOptional.get();
		Assertions.assertThat(foundUser)
		.usingRecursiveComparison()
		.ignoringFields("userId")
		.isEqualTo(user);
		
	}
}
