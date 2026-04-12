package com.killeen.taskflow.components.tasklisttemplate.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.killeen.taskflow.components.tasklisttemplate.model.TaskTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskTemplateResponse;
import com.killeen.taskflow.db.model.generated.TaskTemplateDb;

@Mapper(componentModel = "spring", uses = SubtaskTemplateConverter.class)
public interface TaskTemplateConverter {

    @Mapping(target = "subtaskTemplates", ignore = true)
    TaskTemplate         toDto(TaskTemplateDb db);

    TaskTemplateDb       toDb(TaskTemplate dto);

    TaskTemplateResponse toResponse(TaskTemplate taskTemplate);
}
