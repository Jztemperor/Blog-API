package com.jez.blog.utility;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class RSAKeyProperties {
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	
	public RSAKeyProperties() {
		KeyPair keyPair = KeyGenerator.generateRSAKey();
		publicKey = (RSAPublicKey) keyPair.getPublic();
		privateKey = (RSAPrivateKey) keyPair.getPrivate();
	}
}
