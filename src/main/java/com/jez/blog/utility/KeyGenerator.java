package com.jez.blog.utility;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {
	
	public static KeyPair generateRSAKey() {
		KeyPair keyPair = null;
		
			try {
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
				keyPairGenerator.initialize(2048);
				keyPair = keyPairGenerator.generateKeyPair();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
		return keyPair;
	}
}
