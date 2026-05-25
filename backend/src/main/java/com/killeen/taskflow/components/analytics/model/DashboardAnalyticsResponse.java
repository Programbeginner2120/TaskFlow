package com.killeen.taskflow.components.analytics.model;

import java.util.List;

import com.killeen.taskflow.components.task.model.Task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class DashboardAnalyticsResponse {
    
    private List<Task> tasks;

}
