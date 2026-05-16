package com.killeen.taskflow.components.refreshtoken;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import com.killeen.taskflow.components.refreshtoken.exception.InvalidRefreshTokenException;
import com.killeen.taskflow.components.refreshtoken.exception.RefreshTokenNotFoundException;
import com.killeen.taskflow.components.refreshtoken.model.RefreshToken;
import com.killeen.taskflow.components.refreshtoken.repository.RefreshTokenRepository;
import com.killeen.taskflow.components.refreshtoken.service.RefreshTokenService;
import com.killeen.taskflow.util.HashUtils;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    HashUtils hashUtils;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private Environment env;
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        hashUtils = new HashUtils(env);
        ReflectionTestUtils.setField(refreshTokenService, "hashUtils", hashUtils);
        ReflectionTestUtils.setField(refreshTokenService, "expiration", 3600L);
    }

    @Test
    void createRefreshToken_returnsRawTokenInSelectorDotValidatorFormat() {
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> {
            RefreshToken t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        String raw = refreshTokenService.createRefreshToken(42L);
        assertThat(raw).contains(".");
        String[] parts = raw.split("\\.", 2);
        assertThat(parts[0]).matches("[0-9a-f]+");
        assertThat(parts[1]).matches("[0-9a-f]+");
        assertThat(parts[0].length()).isEqualTo(32);
        assertThat(parts[1].length()).isEqualTo(64);
    }

    @Test
    void createRefreshToken_savesHashedValidatorNotRawValidator() {
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> {
            RefreshToken t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        String raw = refreshTokenService.createRefreshToken(7L);
        String rawValidator = raw.split("\\.", 2)[1];

        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken saved = captor.getValue();

        assertThat(saved.getToken()).isNotEqualTo(rawValidator);
        assertThat(saved.getToken()).isEqualTo(hashUtils.sha256(rawValidator));
    }

    @Test
    void createRefreshToken_setsCorrectUserId() {
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> {
            RefreshToken t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        refreshTokenService.createRefreshToken(123L);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(123L);
    }

    @Test
    void createRefreshToken_setsExpiresAtInFuture() {
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> {
            RefreshToken t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        refreshTokenService.createRefreshToken(1L);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        OffsetDateTime expires = captor.getValue().getExpiresAt();
        assertThat(expires).isAfter(OffsetDateTime.now(ZoneOffset.UTC));
    }

    @Test
    void createRefreshToken_eachCallProducesUniqueTokens() {
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> {
            RefreshToken t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        String t1 = refreshTokenService.createRefreshToken(1L);
        String t2 = refreshTokenService.createRefreshToken(1L);
        assertThat(t1).isNotEqualTo(t2);
    }

    @Test
    void findByToken_nullInput_throwsInvalidRefreshTokenException() {
        when(env.getProperty("refreshtoken.token.null.or.blank")).thenReturn("token null or blank");
        assertThatThrownBy(() -> refreshTokenService.findByToken(null))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessage("token null or blank");
    }

    @Test
    void findByToken_blankInput_throwsInvalidRefreshTokenException() {
        when(env.getProperty("refreshtoken.token.null.or.blank")).thenReturn("token null or blank");
        assertThatThrownBy(() -> refreshTokenService.findByToken(""))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessage("token null or blank");
    }

    @Test
    void findByToken_noDot_throwsInvalidRefreshTokenException() {
        when(env.getProperty("refreshtoken.token.invalid.or.unknown")).thenReturn("invalid or unknown");
        assertThatThrownBy(() -> refreshTokenService.findByToken("abcd"))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessage("invalid or unknown");
    }

    @Test
    void findByToken_selectorNotFound_throwsRefreshTokenNotFoundException() {
        when(refreshTokenRepository.findBySelector(anyString())).thenReturn(java.util.Optional.empty());
        when(env.getProperty("refreshtoken.token.not.found")).thenReturn("not found");

        assertThatThrownBy(() -> refreshTokenService.findByToken("sel.val"))
            .isInstanceOf(RefreshTokenNotFoundException.class)
            .hasMessage("not found");
    }

    @Test
    void findByToken_storedTokenIsNull_throwsInvalidRefreshTokenException() {
        RefreshToken stored = RefreshToken.builder().selector("sel").token(null).userId(1L).build();
        when(refreshTokenRepository.findBySelector("sel")).thenReturn(java.util.Optional.of(stored));
        when(env.getProperty("refreshtoken.token.invalid.or.unknown")).thenReturn("invalid or unknown");

        assertThatThrownBy(() -> refreshTokenService.findByToken("sel.val"))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessage("invalid or unknown");
    }

    @Test
    void findByToken_validatorHashMismatch_throwsInvalidRefreshTokenException() {
        String validatorRaw = "a".repeat(64);
        RefreshToken stored = RefreshToken.builder().selector("sel").token("differenthash").userId(1L).build();
        when(refreshTokenRepository.findBySelector("sel")).thenReturn(java.util.Optional.of(stored));
        when(env.getProperty("refreshtoken.token.invalid.or.unknown")).thenReturn("invalid or unknown");

        assertThatThrownBy(() -> refreshTokenService.findByToken("sel." + validatorRaw))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessage("invalid or unknown");
    }

    @Test
    void findByToken_validToken_returnsStoredRefreshToken() {
        String validatorRaw = "b".repeat(64);
        String expectedHash = hashUtils.sha256(validatorRaw);
        RefreshToken stored = RefreshToken.builder().selector("sel2").token(expectedHash).userId(5L).build();
        when(refreshTokenRepository.findBySelector("sel2")).thenReturn(java.util.Optional.of(stored));

        RefreshToken result = refreshTokenService.findByToken("sel2." + validatorRaw);
        assertThat(result).isEqualTo(stored);
    }

    @Test
    void deleteByToken_nullInput_returnsWithoutCallingRepository() {
        refreshTokenService.deleteByToken(null);
        verify(refreshTokenRepository, never()).deleteBySelector(anyString());
    }

    @Test
    void deleteByToken_blankInput_returnsWithoutCallingRepository() {
        refreshTokenService.deleteByToken("");
        verify(refreshTokenRepository, never()).deleteBySelector(anyString());
    }

    @Test
    void deleteByToken_noDot_throwsInvalidRefreshTokenException() {
        when(env.getProperty("refreshtoken.token.invalid.or.unknown")).thenReturn("invalid or unknown");
        assertThatThrownBy(() -> refreshTokenService.deleteByToken("abcd"))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessage("invalid or unknown");
    }

    @Test
    void deleteByToken_validToken_deletesSelector() {
        refreshTokenService.deleteByToken("sel.val");
        verify(refreshTokenRepository).deleteBySelector("sel");
    }

    @Test
    void deleteAllForUser_delegatesToRepository() {
        refreshTokenService.deleteAllForUser(42L);
        verify(refreshTokenRepository).deleteByUserId(42L);
    }

    @Test
    void deleteExpiredTokens_delegatesToRepository() {
        refreshTokenService.deleteExpiredTokens();
        verify(refreshTokenRepository).deleteExpired();
    }

    @Test
    void isTokenExpired_nullExpiresAt_throwsInvalidRefreshTokenException() {
        when(env.getProperty("refreshtoken.token.null.or.blank")).thenReturn("token null or blank");
        RefreshToken token = RefreshToken.builder().expiresAt(null).build();
        assertThatThrownBy(() -> refreshTokenService.isTokenExpired(token))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessage("token null or blank");
    }

    @Test
    void isTokenExpired_expiresAtInPast_returnsTrue() {
        RefreshToken token = RefreshToken.builder().expiresAt(OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(1)).build();
        assertThat(refreshTokenService.isTokenExpired(token)).isTrue();
    }

    @Test
    void isTokenExpired_expiresAtInFuture_returnsFalse() {
        RefreshToken token = RefreshToken.builder().expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1)).build();
        assertThat(refreshTokenService.isTokenExpired(token)).isFalse();
    }

}
