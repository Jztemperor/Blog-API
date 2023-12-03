package com.jez.blog.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jez.blog.model.Role;
import com.jez.blog.model.User;
import com.jez.blog.repository.RoleRepository;
import com.jez.blog.repository.UserRepository;


@Configuration
public class SeedDataConfiguration {
	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public SeedDataConfiguration(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
    @Bean
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository)
    {
        return args ->
        {
            // If there is already an admin role, return
            if(roleRepository.findByAuthority("ADMIN").isPresent()) return;

            // Create and save roles
            Role adminRole = roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("USER"));

            // Add authorities for the admin user
            Set<Role> authorities = new HashSet<>();
            authorities.add(adminRole);

            // Create admin user
            User admin = userRepository.save(new User("admin", "admin@domain.com", passwordEncoder.encode("password"), authorities));
            userRepository.save(admin);
        };
    }
}
