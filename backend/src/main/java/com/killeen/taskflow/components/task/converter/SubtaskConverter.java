package com.killeen.taskflow.components.task.converter;

import org.mapstruct.Mapper;

import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.SubtaskResponse;
import com.killeen.taskflow.db.model.generated.SubtaskDb;

@Mapper(componentModel = "spring")
public interface SubtaskConverter {
    Subtask         toDto(SubtaskDb db);
    SubtaskDb       toDb(Subtask dto);
    SubtaskResponse toResponse(Subtask subtask);
}
