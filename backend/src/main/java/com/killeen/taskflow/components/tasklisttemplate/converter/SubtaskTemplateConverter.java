package com.killeen.taskflow.components.tasklisttemplate.converter;

import org.mapstruct.Mapper;

import com.killeen.taskflow.components.tasklisttemplate.model.SubtaskTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.SubtaskTemplateResponse;
import com.killeen.taskflow.db.model.generated.SubtaskTemplateDb;

@Mapper(componentModel = "spring")
public interface SubtaskTemplateConverter {
    SubtaskTemplate         toDto(SubtaskTemplateDb db);
    SubtaskTemplateDb       toDb(SubtaskTemplate dto);
    SubtaskTemplateResponse toResponse(SubtaskTemplate subtaskTemplate);
}
