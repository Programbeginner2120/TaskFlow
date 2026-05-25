package com.killeen.taskflow.components.analytics.model;

import java.util.List;

import com.killeen.taskflow.components.analytics.constants.DashboardAnalyticsConstants.TaskDataDuration;
import com.killeen.taskflow.components.analytics.constants.DashboardAnalyticsConstants.TaskDataStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DashboardAnalyticsRequest {
    
    @NotNull
    private TaskDataDuration durationSelection;
    @NotNull
    private TaskDataStatus statusSelection;
    private List<Long> listSelections;

}
