package com.killeen.taskflow.components.tasklisttemplate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSubtaskTemplateRequest {

    @NotBlank
    @Size(max = 1000)
    private String title;
}
