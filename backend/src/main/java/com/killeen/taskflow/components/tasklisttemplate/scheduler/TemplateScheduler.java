package com.killeen.taskflow.components.tasklisttemplate.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.killeen.taskflow.components.tasklisttemplate.service.TaskListTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TemplateScheduler {

    private final TaskListTemplateService taskListTemplateService;

    @Scheduled(cron = "0 * * * * ?")
    public void generateDueTemplates() {
        log.debug("Checking for due templates");
        taskListTemplateService.generateDueTemplates();
    }
}
