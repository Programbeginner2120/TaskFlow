package com.killeen.taskflow.components.tasklist.converter;

import org.mapstruct.Mapper;

import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.components.tasklist.model.TaskListResponse;
import com.killeen.taskflow.db.model.generated.TaskListDb;

@Mapper(componentModel = "spring")
public interface TaskListConverter {
    TaskList         toDto(TaskListDb db);
    TaskListDb       toDb(TaskList dto);
    TaskListResponse toResponse(TaskList taskList);
}
