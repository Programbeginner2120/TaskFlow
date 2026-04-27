package com.killeen.taskflow.components.task.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubtaskResponse {
    private Long          id;
    private Long          taskId;
    private String        title;
    private boolean       completed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
