package com.killeen.taskflow.components.task.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.killeen.taskflow.components.task.TaskEncryptionHelper;
import com.killeen.taskflow.components.task.exception.SubtaskNotFoundException;
import com.killeen.taskflow.components.task.exception.TaskNotFoundException;
import com.killeen.taskflow.components.task.model.CreateSubtaskRequest;
import com.killeen.taskflow.components.task.model.CreateTaskRequest;
import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.components.task.model.UpdateSubtaskRequest;
import com.killeen.taskflow.components.task.model.UpdateTaskRequest;
import com.killeen.taskflow.components.task.repository.SubtaskRepository;
import com.killeen.taskflow.components.task.repository.TaskRepository;
import com.killeen.taskflow.components.tasklist.exception.TaskListNotFoundException;
import com.killeen.taskflow.components.tasklist.repository.TaskListRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository       taskRepository;
    private final SubtaskRepository    subtaskRepository;
    private final TaskListRepository   taskListRepository;
    private final TaskEncryptionHelper encryptionHelper;
    private final Environment          env;

    // -------------------------------------------------------------------------
    // Tasks
    // -------------------------------------------------------------------------

    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findAllByUserId(userId).stream()
                .map(encryptionHelper::decryptTask)
                .toList();
    }

    public Task createTask(Long userId, CreateTaskRequest request) {
        Long taskPosition = null;
        if (request.getListId() != null) {
                taskListRepository.findByIdAndUserId(request.getListId(), userId)
                        .orElseThrow(() -> new TaskListNotFoundException(
                                env.getProperty("task.list.not.found")));

                taskPosition = (long) taskRepository.findByTaskListId(request.getListId())
                        .orElse(List.of()).size();
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Task plaintext = Task.builder()
                .userId(userId)
                .listId(request.getListId())
                .title(request.getTitle())
                .notes(request.getNotes())
                .completed(false)
                .dueDate(request.getDueDate())
                .subtasks(List.of())
                .position(taskPosition)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long id = taskRepository.save(encryptionHelper.encryptTask(plaintext));
        plaintext.setId(id);
        log.info("Created task {} for user {}", id, userId);
        return plaintext;
    }

    @Transactional
    public Task updateTask(Long userId, Long taskId, UpdateTaskRequest request) {
        Task existing = taskRepository.findByIdAndUserId(taskId, userId)
                .map(encryptionHelper::decryptTask)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

        if (request.getListId() != null && !request.getListId().equals(existing.getListId())) {
            taskListRepository.findByIdAndUserId(request.getListId(), userId)
                    .orElseThrow(() -> new TaskListNotFoundException(
                            env.getProperty("task.list.not.found")));
        }

        if (request.getPosition() != null && existing.getListId() != null
                && existing.getPosition() != null
                && !request.getPosition().equals(existing.getPosition())) {

                        long oldPos = existing.getPosition();
                        long newPos = Math.max(0, request.getPosition());

                        List<Task> siblings = taskRepository.findByTaskListId(existing.getListId())
                                .orElse(List.of());

                        // We have to shift elements in range (oldPos, newPos] -1
                        if (oldPos < newPos) {
                                siblings.stream()
                                        .filter(t -> !t.getId().equals(taskId))
                                        .filter(t -> t.getPosition() > oldPos && t.getPosition() <= newPos)
                                        .forEach(t -> {
                                                t.setPosition(t.getPosition() - 1);
                                                taskRepository.update(encryptionHelper.encryptTask(t));
                                        });
                        }
                        // We have to shift elements in range [newPos, oldPos) +1
                        else {
                                siblings.stream()
                                        .filter(t -> !t.getId().equals(taskId))
                                        .filter(t -> t.getPosition() >= newPos && t.getPosition() < oldPos)
                                        .forEach(t -> {
                                                t.setPosition(t.getPosition() + 1);
                                                taskRepository.update(encryptionHelper.encryptTask(t));
                                        });
                        }

                        existing.setPosition(taskId);
        }

        existing.setTitle(request.getTitle());
        existing.setNotes(request.getNotes());
        existing.setDueDate(request.getDueDate());
        existing.setListId(request.getListId());
        existing.setCompleted(request.isCompleted());
        existing.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        taskRepository.update(encryptionHelper.encryptTask(existing));
        log.info("Updated task {} for user {}", taskId, userId);
        return existing;
    }

    public void deleteTask(Long userId, Long taskId) {
        Task existing = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

        // If the task is a part of a list, decrement the positions of all tasks that come after it
        if (existing.getListId() != null) {
                taskRepository.findByTaskListId(existing.getListId())
                        .orElse(List.of())
                        .stream()
                        .filter(t -> !t.getId().equals(existing.getId()))
                        .filter(t -> t.getPosition() > existing.getPosition())
                        .forEach(t -> {
                                t.setPosition(t.getPosition() - 1);
                                taskRepository.update(encryptionHelper.encryptTask(t));
                        });
        }

        taskRepository.deleteById(taskId);
        log.info("Deleted task {} for user {}", taskId, userId);
    }

    // -------------------------------------------------------------------------
    // Subtasks
    // -------------------------------------------------------------------------

    public Subtask createSubtask(Long userId, Long taskId, CreateSubtaskRequest request) {
        taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Subtask plaintext = Subtask.builder()
                .taskId(taskId)
                .title(request.getTitle())
                .completed(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long id = subtaskRepository.save(encryptionHelper.encryptSubtask(plaintext));
        plaintext.setId(id);
        log.info("Created subtask {} on task {}", id, taskId);
        return plaintext;
    }

    @Transactional
    public Subtask updateSubtask(Long userId, Long taskId, Long subtaskId,
                                  UpdateSubtaskRequest request) {
        taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

        Subtask existing = subtaskRepository.findByIdAndTaskId(subtaskId, taskId)
                .map(encryptionHelper::decryptSubtask)
                .orElseThrow(() -> new SubtaskNotFoundException(
                        env.getProperty("subtask.not.found")));

        existing.setTitle(request.getTitle());
        existing.setCompleted(request.isCompleted());
        existing.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        subtaskRepository.update(encryptionHelper.encryptSubtask(existing));
        log.info("Updated subtask {} on task {}", subtaskId, taskId);
        return existing;
    }

    public void deleteSubtask(Long userId, Long taskId, Long subtaskId) {
        taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

        subtaskRepository.findByIdAndTaskId(subtaskId, taskId)
                .orElseThrow(() -> new SubtaskNotFoundException(
                        env.getProperty("subtask.not.found")));

        subtaskRepository.deleteById(subtaskId);
        log.info("Deleted subtask {} from task {}", subtaskId, taskId);
    }

    // -------------------------------------------------------------------------
    // (Encryption helpers delegated to TaskEncryptionHelper bean)
    // -------------------------------------------------------------------------
}
