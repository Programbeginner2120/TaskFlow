package com.killeen.taskflow.components.task.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
import com.killeen.taskflow.config.EncryptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository     taskRepository;
    private final SubtaskRepository  subtaskRepository;
    private final TaskListRepository taskListRepository;
    private final EncryptionService  encryptionService;
    private final Environment        env;

    // -------------------------------------------------------------------------
    // Tasks
    // -------------------------------------------------------------------------

    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findAllByUserId(userId).stream()
                .map(this::decryptTask)
                .toList();
    }

    public Task createTask(Long userId, CreateTaskRequest request) {
        if (request.getListId() != null) {
            taskListRepository.findByIdAndUserId(request.getListId(), userId)
                    .orElseThrow(() -> new TaskListNotFoundException(
                            env.getProperty("task.list.not.found")));
        }

        LocalDateTime now = LocalDateTime.now();
        Task plaintext = Task.builder()
                .userId(userId)
                .listId(request.getListId())
                .title(request.getTitle())
                .notes(request.getNotes())
                .completed(false)
                .dueDate(request.getDueDate())
                .subtasks(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long id = taskRepository.save(encryptTask(plaintext));
        plaintext.setId(id);
        log.info("Created task {} for user {}", id, userId);
        return plaintext;
    }

    public Task updateTask(Long userId, Long taskId, UpdateTaskRequest request) {
        Task existing = taskRepository.findByIdAndUserId(taskId, userId)
                .map(this::decryptTask)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

        if (request.getListId() != null && !request.getListId().equals(existing.getListId())) {
            taskListRepository.findByIdAndUserId(request.getListId(), userId)
                    .orElseThrow(() -> new TaskListNotFoundException(
                            env.getProperty("task.list.not.found")));
        }

        existing.setTitle(request.getTitle());
        existing.setNotes(request.getNotes());
        existing.setDueDate(request.getDueDate());
        existing.setListId(request.getListId());
        existing.setCompleted(request.isCompleted());
        existing.setUpdatedAt(LocalDateTime.now());

        taskRepository.update(encryptTask(existing));
        log.info("Updated task {} for user {}", taskId, userId);
        return existing;
    }

    public void deleteTask(Long userId, Long taskId) {
        taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

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

        LocalDateTime now = LocalDateTime.now();
        Subtask plaintext = Subtask.builder()
                .taskId(taskId)
                .title(request.getTitle())
                .completed(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long id = subtaskRepository.save(encryptSubtask(plaintext));
        plaintext.setId(id);
        log.info("Created subtask {} on task {}", id, taskId);
        return plaintext;
    }

    public Subtask updateSubtask(Long userId, Long taskId, Long subtaskId,
                                  UpdateSubtaskRequest request) {
        taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException(
                        env.getProperty("task.not.found")));

        Subtask existing = subtaskRepository.findByIdAndTaskId(subtaskId, taskId)
                .map(this::decryptSubtask)
                .orElseThrow(() -> new SubtaskNotFoundException(
                        env.getProperty("subtask.not.found")));

        existing.setTitle(request.getTitle());
        existing.setCompleted(request.isCompleted());
        existing.setUpdatedAt(LocalDateTime.now());

        subtaskRepository.update(encryptSubtask(existing));
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
    // Encryption helpers
    // -------------------------------------------------------------------------

    private Task encryptTask(Task task) {
        return task.toBuilder()
                .title(encryptionService.encrypt(task.getTitle()))
                .notes(encryptionService.encrypt(task.getNotes()))
                .build();
    }

    private Task decryptTask(Task task) {
        return task.toBuilder()
                .title(encryptionService.decrypt(task.getTitle()))
                .notes(encryptionService.decrypt(task.getNotes()))
                .subtasks(task.getSubtasks().stream()
                        .map(this::decryptSubtask)
                        .toList())
                .build();
    }

    private Subtask encryptSubtask(Subtask subtask) {
        return subtask.toBuilder()
                .title(encryptionService.encrypt(subtask.getTitle()))
                .build();
    }

    private Subtask decryptSubtask(Subtask subtask) {
        return subtask.toBuilder()
                .title(encryptionService.decrypt(subtask.getTitle()))
                .build();
    }
}
