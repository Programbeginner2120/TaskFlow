package com.killeen.taskflow.components.tasklisttemplate.model;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskTemplateResponse {
    private Long                        id;
    private Long                        listTemplateId;
    private String                      title;
    private String                      notes;
    private Integer                     dueDateOffset;
    private OffsetDateTime              createdAt;
    private OffsetDateTime              updatedAt;
    private List<SubtaskTemplateResponse> subtaskTemplates;
}
