package com.killeen.taskflow.components.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import com.killeen.taskflow.components.email.model.EmailToken;
import com.killeen.taskflow.components.email.model.EmailTokenType;
import com.killeen.taskflow.components.email.repository.EmailTokenRepository;
import com.killeen.taskflow.components.email.service.EmailTokenService;

@ExtendWith(MockitoExtension.class)
public class EmailTokenServiceTest {

    @Mock
    private EmailTokenRepository emailTokenRepository;
    @Mock
    private Environment env;

    @InjectMocks
    private EmailTokenService emailTokenService;

    @BeforeEach
    void setUp() {
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
    
}
