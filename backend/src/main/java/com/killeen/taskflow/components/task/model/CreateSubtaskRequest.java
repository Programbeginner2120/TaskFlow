package com.killeen.taskflow.components.task.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSubtaskRequest {

    @NotBlank
    @Size(max = 1000)
    private String title;
}
