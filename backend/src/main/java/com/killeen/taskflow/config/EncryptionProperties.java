package com.killeen.taskflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app.encryption")
public class EncryptionProperties {

    /**
     * Encryption password — any strong secret string.
     * Sourced from the ENCRYPTION_PASSWORD environment variable.
     */
    private String password;

    /**
     * Hex-encoded 8-byte (16 hex char) salt.
     * Generate with: openssl rand -hex 8
     * Sourced from the ENCRYPTION_SALT environment variable.
     */
    private String salt;
}
