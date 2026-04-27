package com.killeen.taskflow.components.tasklist.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListResponse {
    private Long          id;
    private Long          userId;
    private String        name;
    private String        color;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
