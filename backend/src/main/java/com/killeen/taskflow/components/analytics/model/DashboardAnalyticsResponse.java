package com.killeen.taskflow.components.analytics.model;

import java.util.List;

import com.killeen.taskflow.components.task.model.TaskResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class DashboardAnalyticsResponse {
    
    private List<TaskResponse> tasks;

}
