package com.killeen.taskflow.components.task.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.killeen.taskflow.components.task.converter.SubtaskConverter;
import com.killeen.taskflow.components.task.converter.TaskConverter;
import com.killeen.taskflow.components.task.model.CreateSubtaskRequest;
import com.killeen.taskflow.components.task.model.CreateTaskRequest;
import com.killeen.taskflow.components.task.model.SubtaskResponse;
import com.killeen.taskflow.components.task.model.TaskResponse;
import com.killeen.taskflow.components.task.model.UpdateSubtaskRequest;
import com.killeen.taskflow.components.task.model.UpdateTaskRequest;
import com.killeen.taskflow.components.task.service.TaskService;
import com.killeen.taskflow.util.AuthUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tasks")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService      taskService;
    private final TaskConverter    taskConverter;
    private final SubtaskConverter subtaskConverter;

    // -------------------------------------------------------------------------
    // Tasks
    // -------------------------------------------------------------------------

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return taskService.getAllTasks(userId).stream()
                .map(taskConverter::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return taskConverter.toResponse(taskService.createTask(userId, request));
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,
                                    @Valid @RequestBody UpdateTaskRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return taskConverter.toResponse(taskService.updateTask(userId, id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        taskService.deleteTask(userId, id);
    }

    // -------------------------------------------------------------------------
    // Subtasks
    // -------------------------------------------------------------------------

    @PostMapping("/{taskId}/subtasks")
    @ResponseStatus(HttpStatus.CREATED)
    public SubtaskResponse createSubtask(@PathVariable Long taskId,
                                          @Valid @RequestBody CreateSubtaskRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return subtaskConverter.toResponse(taskService.createSubtask(userId, taskId, request));
    }

    @PutMapping("/{taskId}/subtasks/{id}")
    public SubtaskResponse updateSubtask(@PathVariable Long taskId,
                                          @PathVariable Long id,
                                          @Valid @RequestBody UpdateSubtaskRequest request) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        return subtaskConverter.toResponse(taskService.updateSubtask(userId, taskId, id, request));
    }

    @DeleteMapping("/{taskId}/subtasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubtask(@PathVariable Long taskId, @PathVariable Long id) {
        Long userId = AuthUtils.getAuthenticatedUserId();
        taskService.deleteSubtask(userId, taskId, id);
    }
}
