package com.killeen.taskflow.components.task.model;

import java.time.LocalDate;
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
public class Task {
    private Long          id;
    private Long          userId;
    private Long          listId;
    private String        title;
    private String        notes;
    private boolean       completed;
    private LocalDate     dueDate;
    private List<Subtask> subtasks;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
