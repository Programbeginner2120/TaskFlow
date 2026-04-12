package com.killeen.taskflow.components.tasklisttemplate.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplateResponse;
import com.killeen.taskflow.db.model.generated.TaskListTemplateDb;

@Mapper(componentModel = "spring", uses = TaskTemplateConverter.class)
public interface TaskListTemplateConverter {

    @Mapping(target = "taskTemplates", ignore = true)
    TaskListTemplate         toDto(TaskListTemplateDb db);

    TaskListTemplateDb       toDb(TaskListTemplate dto);

    TaskListTemplateResponse toResponse(TaskListTemplate taskListTemplate);
}
