package com.killeen.taskflow.components.refreshtoken.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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

    private static final SecureRandom RNG = new SecureRandom();
    private static final int SELECTOR_BYTES = 16;  // 32 hex chars
    private static final int VALIDATOR_BYTES = 32; // 64 hex chars

    /**
     * Creates a new refresh token for the given user.
     * Returns the raw token string.
     * @param userId - the id of the user to generate the id for.
     * @return - the raw token created.
     */
    public String createRefreshToken(Long userId) {
        byte[] selectorBytes = new byte[SELECTOR_BYTES];
        RNG.nextBytes(selectorBytes);
        String selector = HexFormat.of().formatHex(selectorBytes);

        byte[] validatorBytes = new byte[VALIDATOR_BYTES];
        RNG.nextBytes(validatorBytes);
        String validatorRaw = HexFormat.of().formatHex(validatorBytes);

        String validatorHashed = hashUtils.sha256(validatorRaw);

        OffsetDateTime expirationTime = OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(expiration);

        RefreshToken token = RefreshToken.builder()
            .userId(userId)
            .selector(selector)
            .expiresAt(expirationTime)
            .token(validatorHashed)
            .build();

        refreshTokenRepository.save(token);
        return selector + "." + validatorRaw;
    }

    /**
     * Finds a user's refresh token based on their raw token, i.e. [selector].[validator]
     * @param rawToken - raw token in the form [selector].[validator]
     * @return the refresh token object associated with their raw token
     */
    public RefreshToken findByToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new InvalidRefreshTokenException(env.getProperty("refreshtoken.token.null.or.blank"));
        }
        if (!rawToken.contains(".")) {
            throw new InvalidRefreshTokenException(env.getProperty("refreshtoken.token.invalid.or.unknown"));
        }

        String[] parts = rawToken.split("\\.", 2);
        String selector = parts[0];
        String validatorRaw = parts[1];

        RefreshToken stored = refreshTokenRepository.findBySelector(selector)
            .orElseThrow(() -> new RefreshTokenNotFoundException(
                env.getProperty("refreshtoken.token.not.found")));

        if (stored.getToken() == null) {
            throw new InvalidRefreshTokenException(env.getProperty("refreshtoken.token.invalid.or.unknown"));
        }

        String expectedHash = hashUtils.sha256(validatorRaw);
        if (!MessageDigest.isEqual(expectedHash.getBytes(StandardCharsets.UTF_8),
                stored.getToken().getBytes(StandardCharsets.UTF_8))) {
            throw new InvalidRefreshTokenException(env.getProperty("refreshtoken.token.invalid.or.unknown"));
        }

        return stored;
    }

    /**
     * Deletes a user's refresh token given a raw token in the form [selector].[validator]
     * @param rawToken - a raw token in the form [selector].[validator]
     */
    public void deleteByToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        if (!rawToken.contains(".")) {
            throw new InvalidRefreshTokenException(env.getProperty("refreshtoken.token.invalid.or.unknown"));
        }

        String selector = rawToken.split("\\.", 2)[0];
        refreshTokenRepository.deleteBySelector(selector);
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
