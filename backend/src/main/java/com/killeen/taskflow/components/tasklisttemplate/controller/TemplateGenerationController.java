package com.killeen.taskflow.components.tasklisttemplate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.killeen.taskflow.components.tasklisttemplate.service.TaskListTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TemplateGenerationController {

    @Value("${app.cron.secret}")
    private String cronSecret;

    private final TaskListTemplateService taskListTemplateService;

    @PostMapping("/internal/generate-templates")
    public ResponseEntity<Void> generateTemplates(
            @RequestHeader("X-Cron-Secret") String providedSecret) {

        if (!cronSecret.equals(providedSecret)) {
            log.warn("Rejected /internal/generate-templates request: invalid X-Cron-Secret");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        taskListTemplateService.generateDueTemplates();
        return ResponseEntity.ok().build();
    }
}
