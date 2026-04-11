package com.killeen.taskflow.components.tasklist.controller;

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

import com.killeen.taskflow.components.tasklist.converter.TaskListConverter;
import com.killeen.taskflow.components.tasklist.model.CreateTaskListRequest;
import com.killeen.taskflow.components.tasklist.model.TaskListResponse;
import com.killeen.taskflow.components.tasklist.model.UpdateTaskListRequest;
import com.killeen.taskflow.components.tasklist.service.TaskListService;
import com.killeen.taskflow.util.AuthUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/task-lists")
@RequiredArgsConstructor
public class TaskListController {

    private final TaskListService   taskListService;
    private final TaskListConverter taskListConverter;

    @GetMapping
    public ResponseEntity<List<TaskListResponse>> getAllTaskLists() {
        Long userId = AuthUtils.getAuthenticatedUserId();
        List<TaskListResponse> response = taskListService.getAllTaskLists(userId).stream()
                .map(taskListConverter::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TaskListResponse> createTaskList(
            @Valid @RequestBody CreateTaskListRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskListConverter.toResponse(taskListService.createTaskList(userId, request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListResponse> updateTaskList(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskListRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(taskListConverter.toResponse(
                taskListService.updateTaskList(userId, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable Long id) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        taskListService.deleteTaskList(userId, id);
        return ResponseEntity.noContent().build();
    }
}
