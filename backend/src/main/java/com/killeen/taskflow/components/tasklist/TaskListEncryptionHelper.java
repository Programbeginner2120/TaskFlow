package com.killeen.taskflow.components.tasklist;

import org.springframework.stereotype.Component;

import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.config.EncryptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskListEncryptionHelper {

    private final EncryptionService encryptionService;

    public TaskList encrypt(TaskList list) {
        return list.toBuilder()
                .name(encryptionService.encrypt(list.getName()))
                .build();
    }

    public TaskList decrypt(TaskList list) {
        return list.toBuilder()
                .name(encryptionService.decrypt(list.getName()))
                .build();
    }
}
