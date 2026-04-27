package com.killeen.taskflow.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        EncryptionProperties encryptionProperties = new EncryptionProperties();
        encryptionProperties.setPassword("testpassword");
        encryptionProperties.setSalt("deadbeefdeadbeef"); // 16 hex chars, this was a weird choice by copilot I'll just roll with it...
        encryptionService = new EncryptionService(encryptionProperties);
        encryptionService.init();
    }

    @Test
    void encrypt_thenDecrypt_returnsOriginalPlaintext() {
        String plaintext = "This is a secret string!";
        String cipherText = encryptionService.encrypt(plaintext);
        assertThat(encryptionService.decrypt(cipherText)).isEqualTo(plaintext);
    }

    @Test
    void encrypt_nullInput_returnsNull() {
        assertThat(encryptionService.encrypt(null)).isNull();
    }

    @Test 
    void encrypt_nonNullInput_returnsNonNullCipherText() {
        String plaintext = "This is a secret string!";
        assertThat(encryptionService.encrypt(plaintext)).isNotNull();
    }

    @Test
    void decrypt_nullInput_returnsNull() {
        assertThat(encryptionService.decrypt(null)).isNull();
    }

    @Test 
    void decrypt_nonNullInput_returnsNonNullCipherText() {
        String plaintext = "This is a secret string!";
        String cipherText = encryptionService.encrypt(plaintext);
        assertThat(encryptionService.encrypt(cipherText)).isNotNull();
    }

    @Test
    void encrypt_differentCallsProduceDifferentCipherTexts() {
        // Spring's Encryptors.text uses a random IV per call - same plaintext must not produce identical output
        String plaintext = "hello";
        String ct1 = encryptionService.encrypt(plaintext);
        String ct2 = encryptionService.encrypt(plaintext);
        assertThat(ct1).isNotEqualTo(ct2);
    }

    @Test
    void decrypt_corruptedCiphertext_throwsEncryptionException() {
        assertThatThrownBy(() -> encryptionService.decrypt("not-valid-ciphertext"))
            .isInstanceOf(EncryptionException.class);
    }
    
}
