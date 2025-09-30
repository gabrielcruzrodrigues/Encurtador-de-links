package com.gabrielrodrigues.encurtador_de_links.security.key;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGenerator {
    public static KeyPair generateRsaKey() throws IllegalAccessException {
        KeyPair keyPair;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
        return keyPair;
    }
}
