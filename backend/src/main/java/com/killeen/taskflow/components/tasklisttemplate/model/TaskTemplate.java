package com.killeen.taskflow.components.tasklisttemplate.model;

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
public class TaskTemplate {
    private Long                   id;
    private Long                   listTemplateId;
    private String                 title;
    private String                 notes;
    private Integer                dueDateOffset;
    private OffsetDateTime         createdAt;
    private OffsetDateTime         updatedAt;
    private List<SubtaskTemplate>  subtaskTemplates;
}
