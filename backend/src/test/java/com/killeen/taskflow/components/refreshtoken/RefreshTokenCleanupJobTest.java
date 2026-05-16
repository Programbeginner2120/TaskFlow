package com.killeen.taskflow.components.refreshtoken;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.killeen.taskflow.components.refreshtoken.scheduler.RefreshTokenCleanupJob;
import com.killeen.taskflow.components.refreshtoken.service.RefreshTokenService;

@ExtendWith(MockitoExtension.class)
class RefreshTokenCleanupJobTest {

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private RefreshTokenCleanupJob job;

    @Test
    void deleteExpiredTokens_invokesServiceDeleteExpiredTokens() {
        job.deleteExpiredTokens();
        verify(refreshTokenService).deleteExpiredTokens();
    }
}
