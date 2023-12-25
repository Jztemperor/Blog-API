package com.jez.blog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.jez.blog.model.Role;
import com.jez.blog.model.User;
import com.jez.blog.service.impl.TokenServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;
    
    @Mock
    private Authentication authentication;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    public void testGenerateJWT() {
        // Arrange
        String expectedToken = "expectedToken";

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        User user = new User(1L,"username", "email@email.com", "password", roles);

        when(authentication.getPrincipal()).thenReturn(user);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .subject(Long.toString(user.getUserId()))
                .claim("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
                .build();

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(
        	    new Jwt(expectedToken, claims.getIssuedAt(), claims.getExpiresAt(), headers, claims.getClaims()));
        // Act
        String actualToken = tokenService.generateJWT(authentication);

        // Assert
        assertEquals(expectedToken, actualToken);
    }
}