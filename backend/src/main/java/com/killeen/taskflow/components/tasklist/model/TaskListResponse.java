package com.killeen.taskflow.components.tasklist.model;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
