package com.killeen.taskflow.components.task.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.db.model.generated.TaskDb;

@Mapper(componentModel = "spring")
public interface TaskConverter {

    @Mapping(target = "subtasks", ignore = true)
    Task toDto(TaskDb db);

    TaskDb toDb(Task dto);
}
