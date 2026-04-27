package com.killeen.taskflow.components.tasklist.model;

import java.time.OffsetDateTime;

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
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
