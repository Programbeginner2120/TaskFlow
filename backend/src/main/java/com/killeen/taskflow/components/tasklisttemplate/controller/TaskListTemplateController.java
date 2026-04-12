package com.killeen.taskflow.components.tasklisttemplate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.killeen.taskflow.components.tasklisttemplate.converter.TaskListTemplateConverter;
import com.killeen.taskflow.components.tasklisttemplate.model.CreateTaskListTemplateRequest;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplateResponse;
import com.killeen.taskflow.components.tasklisttemplate.model.UpdateTaskListTemplateRequest;
import com.killeen.taskflow.components.tasklisttemplate.service.TaskListTemplateService;
import com.killeen.taskflow.util.AuthUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/task-list-templates")
@RequiredArgsConstructor
public class TaskListTemplateController {

    private final TaskListTemplateService   taskListTemplateService;
    private final TaskListTemplateConverter taskListTemplateConverter;

    @GetMapping
    public ResponseEntity<List<TaskListTemplateResponse>> getAllTemplates() {
        Long userId = AuthUtils.getAuthenticatedUserId();
        List<TaskListTemplateResponse> response = taskListTemplateService.getAllTemplates(userId).stream()
                .map(taskListTemplateConverter::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TaskListTemplateResponse> createTemplate(
            @Valid @RequestBody CreateTaskListTemplateRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskListTemplateConverter.toResponse(
                        taskListTemplateService.createTemplate(userId, request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListTemplateResponse> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskListTemplateRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(taskListTemplateConverter.toResponse(
                taskListTemplateService.updateTemplate(userId, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        taskListTemplateService.deleteTemplate(userId, id);
        return ResponseEntity.noContent().build();
    }
}
