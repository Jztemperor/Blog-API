package com.jez.blog.service.impl;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.jez.blog.model.User;
import com.jez.blog.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	private final JwtEncoder jwtEncoder;
	
	public TokenServiceImpl(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	@Override
	public String generateJWT(Authentication authentication) {
        // Get current time
        Instant now = Instant.now();

        // Get user's authorities
        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        // Get user's id
        Long userId = ( (User) authentication.getPrincipal()).getUserId();
        
        // Set claim parameters
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(Long.toString(userId)) // Pass user id to token
                .claim("roles", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

}
