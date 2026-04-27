package com.killeen.taskflow.components.tasklisttemplate;

import org.springframework.stereotype.Component;

import com.killeen.taskflow.components.tasklisttemplate.model.SubtaskTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskTemplate;
import com.killeen.taskflow.config.EncryptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskListTemplateEncryptionHelper {

    private final EncryptionService encryptionService;

    public TaskListTemplate encrypt(TaskListTemplate template) {
        return template.toBuilder()
                .name(encryptionService.encrypt(template.getName()))
                .build();
    }

    public TaskListTemplate decrypt(TaskListTemplate template) {
        return template.toBuilder()
                .name(encryptionService.decrypt(template.getName()))
                .build();
    }

    public TaskTemplate encrypt(TaskTemplate template) {
        return template.toBuilder()
                .title(encryptionService.encrypt(template.getTitle()))
                .notes(encryptionService.encrypt(template.getNotes()))
                .build();
    }

    public TaskTemplate decrypt(TaskTemplate template) {
        return template.toBuilder()
                .title(encryptionService.decrypt(template.getTitle()))
                .notes(encryptionService.decrypt(template.getNotes()))
                .build();
    }

    public SubtaskTemplate encrypt(SubtaskTemplate template) {
        return template.toBuilder()
                .title(encryptionService.encrypt(template.getTitle()))
                .build();
    }

    public SubtaskTemplate decrypt(SubtaskTemplate template) {
        return template.toBuilder()
                .title(encryptionService.decrypt(template.getTitle()))
                .build();
    }
}
