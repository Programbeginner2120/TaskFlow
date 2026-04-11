package com.killeen.taskflow.components.tasklist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.killeen.taskflow.components.tasklist.model.CreateTaskListRequest;
import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.components.tasklist.model.TaskListResponse;
import com.killeen.taskflow.components.tasklist.model.UpdateTaskListRequest;
import com.killeen.taskflow.components.tasklist.service.TaskListService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/task-lists")
@RequiredArgsConstructor
public class TaskListController {

    private final TaskListService taskListService;

    @GetMapping
    public ResponseEntity<List<TaskListResponse>> getAllTaskLists() {
        Long userId = getAuthenticatedUserId();
        List<TaskListResponse> response = taskListService.getAllTaskLists(userId).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TaskListResponse> createTaskList(
            @Valid @RequestBody CreateTaskListRequest request) {
        Long userId = getAuthenticatedUserId();
        TaskList created = taskListService.createTaskList(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListResponse> updateTaskList(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskListRequest request) {
        Long userId = getAuthenticatedUserId();
        TaskList updated = taskListService.updateTaskList(userId, id, request);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        taskListService.deleteTaskList(userId, id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }

    private TaskListResponse toResponse(TaskList taskList) {
        return TaskListResponse.builder()
                .id(taskList.getId())
                .userId(taskList.getUserId())
                .name(taskList.getName())
                .color(taskList.getColor())
                .createdAt(taskList.getCreatedAt())
                .updatedAt(taskList.getUpdatedAt())
                .build();
    }
}
