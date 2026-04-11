package com.killeen.taskflow.components.tasklist.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaskList {
    private Long          id;
    private Long          userId;
    private String        name;
    private String        color;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
