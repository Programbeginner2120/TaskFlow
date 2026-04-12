package com.killeen.taskflow.components.tasklist.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.killeen.taskflow.components.tasklist.TaskListEncryptionHelper;
import com.killeen.taskflow.components.tasklist.exception.TaskListNotFoundException;
import com.killeen.taskflow.components.tasklist.model.CreateTaskListRequest;
import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.components.tasklist.model.UpdateTaskListRequest;
import com.killeen.taskflow.components.tasklist.repository.TaskListRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskListService {

    private final TaskListRepository      taskListRepository;
    private final TaskListEncryptionHelper encryptionHelper;
    private final Environment              env;

    public List<TaskList> getAllTaskLists(Long userId) {
        return taskListRepository.findAllByUserId(userId).stream()
                .map(encryptionHelper::decrypt)
                .toList();
    }

    public TaskList createTaskList(Long userId, CreateTaskListRequest request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        TaskList plaintext = TaskList.builder()
                .userId(userId)
                .name(request.getName())
                .color(request.getColor() != null ? request.getColor() : "#6366f1")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long id = taskListRepository.save(encryptionHelper.encrypt(plaintext));
        plaintext.setId(id);
        log.info("Created task list {} for user {}", id, userId);
        return plaintext;
    }

    public TaskList updateTaskList(Long userId, Long taskListId, UpdateTaskListRequest request) {
        TaskList existing = taskListRepository.findByIdAndUserId(taskListId, userId)
                .map(encryptionHelper::decrypt)
                .orElseThrow(() -> new TaskListNotFoundException(
                        env.getProperty("task.list.not.found")));

        existing.setName(request.getName());
        existing.setColor(request.getColor() != null ? request.getColor() : existing.getColor());
        existing.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        taskListRepository.update(encryptionHelper.encrypt(existing));
        log.info("Updated task list {} for user {}", taskListId, userId);
        return existing;
    }

    public void deleteTaskList(Long userId, Long taskListId) {
        taskListRepository.findByIdAndUserId(taskListId, userId)
                .orElseThrow(() -> new TaskListNotFoundException(
                        env.getProperty("task.list.not.found")));

        taskListRepository.deleteById(taskListId);
        log.info("Deleted task list {} for user {}", taskListId, userId);
    }

    // (Encryption helpers delegated to TaskListEncryptionHelper bean)
}
