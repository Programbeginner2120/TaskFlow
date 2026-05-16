package com.killeen.taskflow.components.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import com.killeen.taskflow.components.email.exception.InvalidEmailTokenException;
import com.killeen.taskflow.components.email.model.EmailToken;
import com.killeen.taskflow.components.email.model.EmailTokenType;
import com.killeen.taskflow.components.email.repository.EmailTokenRepository;
import com.killeen.taskflow.components.email.service.EmailTokenService;
import com.killeen.taskflow.util.HashUtils;

@ExtendWith(MockitoExtension.class)
public class EmailTokenServiceTest {

    HashUtils hashUtils;

    @Mock
    private EmailTokenRepository emailTokenRepository;
    @Mock
    private Environment env;

    @InjectMocks
    private EmailTokenService emailTokenService;

    @BeforeEach
    void setUp() {
        hashUtils = new HashUtils(env);
        ReflectionTestUtils.setField(emailTokenService, "hashUtils", hashUtils);
        ReflectionTestUtils.setField(emailTokenService, "verificationTtlHours", 24);
        ReflectionTestUtils.setField(emailTokenService, "resetTtlHours", 1);
    }

    @Test
    void createToken_savesHashedToken_nowRawUuid() {
        ArgumentCaptor<EmailToken> captor = ArgumentCaptor.forClass(EmailToken.class);

        String rawToken = emailTokenService.createToken(1L, EmailTokenType.VERIFY_EMAIL);

        verify(emailTokenRepository).save(captor.capture());
        EmailToken saved = captor.getValue();
        // The standard token myst NOT be the raw UUID
        assertThat(saved.getToken()).isNotEqualTo(rawToken);
        // It should be a 64-char SHA-256 hash
        assertThat(saved.getToken()).matches("[a-f0-9]{64}");
    }

    @Test
    void validateAndConsume_validToken_returnsEmailToken() {
        String rawToken = "some-raw-token";
        EmailToken stored = EmailToken.builder()
                .id(1L)
                .userId(1L)
                .tokenType(EmailTokenType.VERIFY_EMAIL)
                .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1))
                .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(stored));
        

        EmailToken result = emailTokenService.validateAndConsume(rawToken, EmailTokenType.VERIFY_EMAIL);

        assertThat(result.getUserId()).isEqualTo(1L);
        verify(emailTokenRepository).markUsed(1L);
    }

    @Test
    void validateAndConsume_expiredToken_throwsInvalidTokenException() {
        EmailToken expired = EmailToken.builder()
                    .id(1L)
                    .userId(1L)
                    .tokenType(EmailTokenType.VERIFY_EMAIL)
                    .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).minusHours(1))
                    .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(expired));
        when(env.getProperty(any())).thenReturn("Token has expired");

        assertThatThrownBy(() -> 
                    emailTokenService.validateAndConsume("raw", EmailTokenType.VERIFY_EMAIL))
                    .isInstanceOf(InvalidEmailTokenException.class)
                    .hasMessage("Token has expired");

    }

    @Test
    void validateAndConsume_alreadyUsedToken_throwsInvalidTokenException() {
        EmailToken used = EmailToken.builder()
                    .id(1L)
                    .userId(1L)
                    .tokenType(EmailTokenType.VERIFY_EMAIL)
                    .usedAt(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(5))
                    .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1))
                    .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(used));
        when(env.getProperty(any())).thenReturn("Already used");

        assertThatThrownBy(() -> 
                emailTokenService.validateAndConsume("raw", EmailTokenType.VERIFY_EMAIL))
                .isInstanceOf(InvalidEmailTokenException.class)
                .hasMessage("Already used");
    }

    @Test
    void validateAndConsume_wrongType_throwsInvalidTokenException() {
        EmailToken wrongType = EmailToken.builder()
                        .id(1L)
                        .userId(1L)
                        .tokenType(EmailTokenType.RESET_PASSWORD)
                        .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1))
                        .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(wrongType));
        when(env.getProperty(any())).thenReturn("Wrong type");

        assertThatThrownBy(() -> 
                    emailTokenService.validateAndConsume("raw", EmailTokenType.VERIFY_EMAIL))
                    .isInstanceOf(InvalidEmailTokenException.class)
                    .hasMessage("Wrong type");
    }
    
}
