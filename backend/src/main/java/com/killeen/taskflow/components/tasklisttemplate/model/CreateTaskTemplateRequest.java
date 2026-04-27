package com.killeen.taskflow.components.tasklisttemplate.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTaskTemplateRequest {

    @NotBlank
    @Size(max = 1000)
    private String title;

    private String  notes;
    private Integer dueDateOffset;

    @Valid
    private List<CreateSubtaskTemplateRequest> subtaskTemplates;
}
