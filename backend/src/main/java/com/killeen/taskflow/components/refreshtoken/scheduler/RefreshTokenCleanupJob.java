package com.killeen.taskflow.components.refreshtoken.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.killeen.taskflow.components.refreshtoken.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupJob {

    private final RefreshTokenService refreshTokenService;

    @Scheduled(cron = "${refresh.cleanup.cron:0 0 * * * *}")
    public void deleteExpiredTokens() {
        refreshTokenService.deleteExpiredTokens();
    }
}
