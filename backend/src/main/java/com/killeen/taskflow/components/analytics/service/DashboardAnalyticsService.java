package com.killeen.taskflow.components.analytics.service;

import org.springframework.stereotype.Service;

import com.killeen.taskflow.components.analytics.model.DashboardAnalyticsRequest;
import com.killeen.taskflow.components.analytics.model.DashboardAnalyticsResponse;
import com.killeen.taskflow.components.task.service.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardAnalyticsService {

    private final TaskService taskService;

    public DashboardAnalyticsResponse retrieveDashboardAnalyticsData(Long userId, DashboardAnalyticsRequest dashboardAnalyticsRequest) {
        return taskService.retrieveDashboardAnalyticsData(userId, dashboardAnalyticsRequest);
    }
    
}
