package com.killeen.taskflow.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.killeen.taskflow.components.user.model.User;

import static org.assertj.core.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("test-secret-key-that-is-at-least-32-bytes-long!");
        properties.setExpiration(3_600_000L);
        jwtService = new JwtService(properties);
        jwtService.init();
    }

    private User testUser() {
        return User.builder()
                .id(1L)
                .email("user@example.com")
                .displayName("Test User")
                .build();
    }

    private JwtService generateShortJwtService() {
        JwtProperties shortProperties = new JwtProperties();
        shortProperties.setSecret("test-secret-key-that-is-at-least-32-bytes-long!");
        shortProperties.setExpiration(-1L);
        JwtService shortJwtService = new JwtService(shortProperties);
        shortJwtService.init();

        return shortJwtService;
    }

    @Test
    void generateToken_validUser_producesNonBlankJwt() {
        String token = jwtService.generateToken(testUser());
        assertThat(token).isNotBlank();
    }

    @Test
    void extractUserId_afterGenerate_returnsSameId() {
        String token = jwtService.generateToken(testUser());
        assertThat(jwtService.extractUserId(token)).isEqualTo(1L);
    }

    @Test
    void extractUserId_afterGenerate_doesNotEqualDifferentId() {
        String token = jwtService.generateToken(testUser());
        assertThat(jwtService.extractUserId(token)).isNotEqualTo(2L);
    }

    @Test
    void isTokenValid_freshToken_returnsTrue() {
        String token = jwtService.generateToken(testUser());
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_expiredToken_returnsFalse() {
        JwtService shortJwtService = this.generateShortJwtService();

        String token = shortJwtService.generateToken(testUser());
        assertThat(shortJwtService.isTokenValid(token)).isFalse();
    }

    @Test
    void isTokenValid_garbageString_returnsFalse() {
        assertThat(jwtService.isTokenValid("not.a.jwt")).isFalse();
    }

    @Test
    void extractEmail_afterGenerate_returnsSameEmail() {
        String token = jwtService.generateToken(testUser());
        assertThat(jwtService.extractEmail(token)).isEqualTo("user@example.com");
    }

}
