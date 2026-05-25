package com.killeen.taskflow.components.analytics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.killeen.taskflow.components.analytics.model.DashboardAnalyticsRequest;
import com.killeen.taskflow.components.analytics.model.DashboardAnalyticsResponse;
import com.killeen.taskflow.components.analytics.service.DashboardAnalyticsService;
import com.killeen.taskflow.util.AuthUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/dashboard-analytics")
@Slf4j
@RequiredArgsConstructor
public class DashboardAnalyticsController {

    private final DashboardAnalyticsService dashboardAnalyticsService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DashboardAnalyticsResponse retrieveDashboardAnalyticsData(@Valid @RequestBody DashboardAnalyticsRequest dashboardAnalyticsRequest) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        log.info("Retrieving dashboard analytics for user {}", userId);
        return this.dashboardAnalyticsService.retrieveDashboardAnalyticsData(userId, dashboardAnalyticsRequest);
    }
    
}
