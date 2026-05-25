package com.killeen.taskflow.components.analytics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.killeen.taskflow.components.analytics.model.DashboardAnalyticsRequest;
import com.killeen.taskflow.components.analytics.model.DashboardAnalyticsResponse;
import com.killeen.taskflow.components.task.converter.TaskConverter;
import com.killeen.taskflow.components.task.model.TaskResponse;
import com.killeen.taskflow.components.task.service.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardAnalyticsService {

    private final TaskService   taskService;
    private final TaskConverter taskConverter;

    public DashboardAnalyticsResponse retrieveDashboardAnalyticsData(Long userId, DashboardAnalyticsRequest dashboardAnalyticsRequest) {
        log.debug("Fetching dashboard analytics data for user {} with request: duration={}, status={}, lists={}",
                userId,
                dashboardAnalyticsRequest.getDurationSelection(),
                dashboardAnalyticsRequest.getStatusSelection(),
                dashboardAnalyticsRequest.getListSelections());
        List<TaskResponse> tasks = taskService.retrieveDashboardAnalyticsData(userId, dashboardAnalyticsRequest)
                .stream()
                .map(taskConverter::toResponse)
                .toList();
        log.debug("Returning {} tasks for user {}", tasks.size(), userId);
        return DashboardAnalyticsResponse.builder()
                .tasks(tasks)
                .build();
    }
    
}
