package com.killeen.taskflow.components.task.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTaskRequest {

    @NotBlank
    @Size(max = 1000)
    private String    title;

    private String    notes;
    private LocalDate dueDate;
    private Long      listId;
}
