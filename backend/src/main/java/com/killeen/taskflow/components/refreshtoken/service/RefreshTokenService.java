package com.killeen.taskflow.components.refreshtoken.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.killeen.taskflow.components.refreshtoken.converter.RefreshTokenConverter;
import com.killeen.taskflow.components.refreshtoken.exception.InvalidRefreshTokenException;
import com.killeen.taskflow.components.refreshtoken.exception.RefreshTokenNotFoundException;
import com.killeen.taskflow.components.refreshtoken.model.RefreshToken;
import com.killeen.taskflow.components.refreshtoken.repository.RefreshTokenRepository;
import com.killeen.taskflow.util.HashUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final HashUtils hashUtils;
    private final Environment env;

    @Value("${refresh.expiration}")
    private long expiration;

    /**
     * Creates a new refresh token for the given user.
     * Returns the raw token string.
     * @param userId - the id of the user to generate the id for.
     * @return - the raw token created.
     */
    public String createRefreshToken(Long userId) {
        String rawToken = UUID.randomUUID().toString();
        String hashedToken = hashUtils.sha256(rawToken);

        OffsetDateTime expirationTime = OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(expiration);

        RefreshToken token = RefreshToken.builder()
            .userId(userId)
            .expiresAt(expirationTime)
            .token(hashedToken)
            .build();

        refreshTokenRepository.save(token);
        return rawToken;
    }

    public RefreshToken findByToken(String rawToken) {
        String hashedToken = hashUtils.sha256(rawToken);
        return refreshTokenRepository.findByToken(hashedToken)
            .orElseThrow(() -> new RefreshTokenNotFoundException(
                env.getProperty("refreshtoken.token.not.found")));
    }

    public void deleteByToken(String rawToken) {
        
    }

    /**
     * Checks if a given token is expired based upon the current time.
     * @param token - the refresh token to be validated.
     * @return - true if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(RefreshToken token) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (token.getExpiresAt() == null) {
            throw new InvalidRefreshTokenException(env.getProperty("refreshtoken.token.null.or.blank"));
        }
        return token.getExpiresAt().isBefore(now);
    }
    
}
