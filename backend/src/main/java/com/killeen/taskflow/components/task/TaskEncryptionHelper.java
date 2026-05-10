package com.killeen.taskflow.components.task;

import org.springframework.stereotype.Component;

import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.config.EncryptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskEncryptionHelper {

    private final EncryptionService encryptionService;

    public Task encryptTask(Task task) {
        return task.toBuilder()
                .title(encryptionService.encrypt(task.getTitle()))
                .notes(encryptionService.encrypt(task.getNotes()))
                .build();
    }

    public Task decryptTask(Task task) {
        return task.toBuilder()
                .title(encryptionService.decrypt(task.getTitle()))
                .notes(encryptionService.decrypt(task.getNotes()))
                .subtasks(task.getSubtasks().stream()
                        .map(this::decryptSubtask)
                        .toList())
                .position(task.getPosition())
                .build();
    }

    public Subtask encryptSubtask(Subtask subtask) {
        return subtask.toBuilder()
                .title(encryptionService.encrypt(subtask.getTitle()))
                .build();
    }

    public Subtask decryptSubtask(Subtask subtask) {
        return subtask.toBuilder()
                .title(encryptionService.decrypt(subtask.getTitle()))
                .build();
    }
}
