package com.killeen.taskflow.components.tasklisttemplate.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTaskListTemplateRequest {

    @Size(max = 255)
    private String name;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "color must be a valid hex color (e.g. #6366f1)")
    private String color;

    private String rrule;

    @Size(min = 1, max = 64)
    private String timezone;

    @Valid
    private List<CreateTaskTemplateRequest> taskTemplates;
}
