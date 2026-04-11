package com.killeen.taskflow.components.tasklist.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTaskListRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "color must be a valid hex color (e.g. #6366f1)")
    private String color;
}
