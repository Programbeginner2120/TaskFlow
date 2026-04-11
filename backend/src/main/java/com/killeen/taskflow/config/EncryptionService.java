package com.killeen.taskflow.config;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EncryptionService {

    private final EncryptionProperties properties;

    private TextEncryptor textEncryptor;

    @PostConstruct
    public void init() {
        this.textEncryptor = Encryptors.text(
                properties.getPassword(),
                properties.getSalt()
        );
        log.info("EncryptionService initialized");
    }

    public String encrypt(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        try {
            return textEncryptor.encrypt(plaintext);
        } catch (Exception e) {
            throw new EncryptionException("Failed to encrypt data", e);
        }
    }

    public String decrypt(String encoded) {
        if (encoded == null) {
            return null;
        }
        try {
            return textEncryptor.decrypt(encoded);
        } catch (Exception e) {
            throw new EncryptionException("Failed to decrypt data", e);
        }
    }
}
