package com.killeen.taskflow.components.tasklisttemplate.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SubtaskTemplate {
    private Long           id;
    private Long           taskTemplateId;
    private String         title;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
