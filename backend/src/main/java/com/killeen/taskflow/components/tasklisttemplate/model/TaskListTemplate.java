package com.killeen.taskflow.components.tasklisttemplate.model;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaskListTemplate {
    private Long               id;
    private Long               userId;
    private String             name;
    private String             generationTitle;
    private String             color;
    private String             rrule;
    private String             timezone;
    private OffsetDateTime     lastGenerated;
    private OffsetDateTime     nextGenerate;
    private OffsetDateTime     createdAt;
    private OffsetDateTime     updatedAt;
    private List<TaskTemplate> taskTemplates;
}
