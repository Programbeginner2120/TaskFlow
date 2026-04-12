package com.killeen.taskflow.components.tasklisttemplate.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.killeen.taskflow.components.task.TaskEncryptionHelper;
import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.components.task.repository.SubtaskRepository;
import com.killeen.taskflow.components.task.repository.TaskRepository;
import com.killeen.taskflow.components.tasklist.TaskListEncryptionHelper;
import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.components.tasklist.repository.TaskListRepository;
import com.killeen.taskflow.components.tasklisttemplate.TaskListTemplateEncryptionHelper;
import com.killeen.taskflow.components.tasklisttemplate.exception.TaskListTemplateNotFoundException;
import com.killeen.taskflow.components.tasklisttemplate.model.CreateSubtaskTemplateRequest;
import com.killeen.taskflow.components.tasklisttemplate.model.CreateTaskListTemplateRequest;
import com.killeen.taskflow.components.tasklisttemplate.model.CreateTaskTemplateRequest;
import com.killeen.taskflow.components.tasklisttemplate.model.SubtaskTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.UpdateTaskListTemplateRequest;
import com.killeen.taskflow.components.tasklisttemplate.repository.SubtaskTemplateRepository;
import com.killeen.taskflow.components.tasklisttemplate.repository.TaskListTemplateRepository;
import com.killeen.taskflow.components.tasklisttemplate.repository.TaskTemplateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.Recur;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskListTemplateService {

    private final TaskListTemplateRepository   templateRepository;
    private final TaskTemplateRepository       taskTemplateRepository;
    private final SubtaskTemplateRepository    subtaskTemplateRepository;
    private final TaskListTemplateEncryptionHelper encryptionHelper;

    private final TaskListRepository           taskListRepository;
    private final TaskRepository               taskRepository;
    private final SubtaskRepository            subtaskRepository;
    private final TaskListEncryptionHelper     taskListEncryptionHelper;
    private final TaskEncryptionHelper         taskEncryptionHelper;

    private final Environment env;

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    public List<TaskListTemplate> getAllTemplates(Long userId) {
        return templateRepository.findAllByUserId(userId).stream()
                .map(encryptionHelper::decrypt)
                .map(this::attachTaskTemplates)
                .toList();
    }

    public TaskListTemplate createTemplate(Long userId, CreateTaskListTemplateRequest request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String timezone = request.getTimezone() != null ? request.getTimezone() : "UTC";
        OffsetDateTime nextGenerate = computeNextGenerate(request.getRrule(), timezone);

        TaskListTemplate plaintext = TaskListTemplate.builder()
                .userId(userId)
                .name(request.getName())
                .color(request.getColor() != null ? request.getColor() : "#6366f1")
                .rrule(request.getRrule())
                .timezone(timezone)
                .nextGenerate(nextGenerate)
                .createdAt(now)
                .updatedAt(now)
                .taskTemplates(List.of())
                .build();

        Long id = templateRepository.save(encryptionHelper.encrypt(plaintext));
        plaintext = plaintext.toBuilder().id(id).build();

        if (request.getTaskTemplates() != null && !request.getTaskTemplates().isEmpty()) {
            Long templateId = id;
            List<TaskTemplate> taskTemplates = request.getTaskTemplates().stream()
                    .map(req -> saveTaskTemplate(templateId, req, now))
                    .toList();
            plaintext = plaintext.toBuilder().taskTemplates(taskTemplates).build();
        }

        log.info("Created task list template {} for user {}", id, userId);
        return plaintext;
    }

    public TaskListTemplate updateTemplate(Long userId, Long id, UpdateTaskListTemplateRequest request) {
        TaskListTemplate existing = templateRepository.findByIdAndUserId(id, userId)
                .map(encryptionHelper::decrypt)
                .orElseThrow(() -> new TaskListTemplateNotFoundException(
                        env.getProperty("task.list.template.not.found")));

        String rrule    = request.getRrule()    != null ? request.getRrule()    : existing.getRrule();
        String timezone = request.getTimezone() != null ? request.getTimezone() : existing.getTimezone();
        boolean scheduleChanged = request.getRrule() != null || request.getTimezone() != null;

        TaskListTemplate updated = existing.toBuilder()
                .name(request.getName()   != null ? request.getName()   : existing.getName())
                .color(request.getColor() != null ? request.getColor()  : existing.getColor())
                .rrule(rrule)
                .timezone(timezone)
                .nextGenerate(scheduleChanged ? computeNextGenerate(rrule, timezone) : existing.getNextGenerate())
                .updatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        templateRepository.update(encryptionHelper.encrypt(updated));

        if (request.getTaskTemplates() != null) {
            taskTemplateRepository.deleteAllByListTemplateId(id);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            List<TaskTemplate> taskTemplates = request.getTaskTemplates().stream()
                    .map(req -> saveTaskTemplate(id, req, now))
                    .toList();
            updated = updated.toBuilder().taskTemplates(taskTemplates).build();
        } else {
            updated = attachTaskTemplates(updated);
        }

        log.info("Updated task list template {} for user {}", id, userId);
        return updated;
    }

    public void deleteTemplate(Long userId, Long id) {
        templateRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskListTemplateNotFoundException(
                        env.getProperty("task.list.template.not.found")));
        templateRepository.deleteById(id);
        log.info("Deleted task list template {} for user {}", id, userId);
    }

    // -------------------------------------------------------------------------
    // Generation
    // -------------------------------------------------------------------------

    public void generateDueTemplates() {
        List<TaskListTemplate> due = templateRepository.findDueTemplates(
                OffsetDateTime.now(ZoneOffset.UTC));
        for (TaskListTemplate template : due) {
            try {
                generateFromTemplate(template);
            } catch (Exception e) {
                log.error("Failed to generate from template {}: {}", template.getId(), e.getMessage(), e);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private TaskListTemplate attachTaskTemplates(TaskListTemplate template) {
        List<TaskTemplate> rawTaskTemplates =
                taskTemplateRepository.findAllByListTemplateId(template.getId()).stream()
                        .map(encryptionHelper::decrypt)
                        .toList();

        if (rawTaskTemplates.isEmpty()) {
            return template.toBuilder().taskTemplates(List.of()).build();
        }

        List<Long> taskTemplateIds = rawTaskTemplates.stream().map(TaskTemplate::getId).toList();
        Map<Long, List<SubtaskTemplate>> subtasksByTaskTemplateId =
                subtaskTemplateRepository.findAllByTaskTemplateIdIn(taskTemplateIds).stream()
                        .map(encryptionHelper::decrypt)
                        .collect(Collectors.groupingBy(SubtaskTemplate::getTaskTemplateId));

        List<TaskTemplate> taskTemplatesWithSubtasks = rawTaskTemplates.stream()
                .map(tt -> tt.toBuilder()
                        .subtaskTemplates(subtasksByTaskTemplateId.getOrDefault(tt.getId(), List.of()))
                        .build())
                .toList();

        return template.toBuilder().taskTemplates(taskTemplatesWithSubtasks).build();
    }

    private TaskTemplate saveTaskTemplate(Long listTemplateId,
                                          CreateTaskTemplateRequest req,
                                          OffsetDateTime now) {
        TaskTemplate plaintext = TaskTemplate.builder()
                .listTemplateId(listTemplateId)
                .title(req.getTitle())
                .notes(req.getNotes())
                .dueDateOffset(req.getDueDateOffset())
                .createdAt(now)
                .updatedAt(now)
                .subtaskTemplates(List.of())
                .build();

        Long id = taskTemplateRepository.save(encryptionHelper.encrypt(plaintext));
        plaintext = plaintext.toBuilder().id(id).build();

        if (req.getSubtaskTemplates() != null && !req.getSubtaskTemplates().isEmpty()) {
            Long taskTemplateId = id;
            List<SubtaskTemplate> subtaskTemplates = req.getSubtaskTemplates().stream()
                    .map(sreq -> saveSubtaskTemplate(taskTemplateId, sreq, now))
                    .toList();
            plaintext = plaintext.toBuilder().subtaskTemplates(subtaskTemplates).build();
        }

        return plaintext;
    }

    private SubtaskTemplate saveSubtaskTemplate(Long taskTemplateId,
                                                 CreateSubtaskTemplateRequest req,
                                                 OffsetDateTime now) {
        SubtaskTemplate plaintext = SubtaskTemplate.builder()
                .taskTemplateId(taskTemplateId)
                .title(req.getTitle())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long id = subtaskTemplateRepository.save(encryptionHelper.encrypt(plaintext));
        return plaintext.toBuilder().id(id).build();
    }

    private void generateFromTemplate(TaskListTemplate encryptedTemplate) {
        TaskListTemplate template = encryptionHelper.decrypt(encryptedTemplate);
        template = attachTaskTemplates(template);

        OffsetDateTime now    = OffsetDateTime.now(ZoneOffset.UTC);
        LocalDate      today  = now.toLocalDate();

        TaskList taskList = TaskList.builder()
                .userId(template.getUserId())
                .name(template.getName())
                .color(template.getColor())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long listId = taskListRepository.save(taskListEncryptionHelper.encrypt(taskList));

        for (TaskTemplate taskTemplate : template.getTaskTemplates()) {
            LocalDate dueDate = taskTemplate.getDueDateOffset() != null
                    ? today.plusDays(taskTemplate.getDueDateOffset())
                    : null;

            Task task = Task.builder()
                    .userId(template.getUserId())
                    .listId(listId)
                    .title(taskTemplate.getTitle())
                    .notes(taskTemplate.getNotes())
                    .completed(false)
                    .dueDate(dueDate)
                    .subtasks(List.of())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            Long taskId = taskRepository.save(taskEncryptionHelper.encryptTask(task));

            for (SubtaskTemplate subtaskTemplate : taskTemplate.getSubtaskTemplates()) {
                Subtask subtask = Subtask.builder()
                        .taskId(taskId)
                        .title(subtaskTemplate.getTitle())
                        .completed(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();
                subtaskRepository.save(taskEncryptionHelper.encryptSubtask(subtask));
            }
        }

        OffsetDateTime nextGenerate = computeNextGenerate(template.getRrule(), template.getTimezone());
        templateRepository.updateSchedule(template.getId(), now, nextGenerate);

        log.info("Generated task list {} from template {} for user {}",
                listId, template.getId(), template.getUserId());
    }

    private OffsetDateTime computeNextGenerate(String rrule, String timezone) {
        try {
            Recur<ZonedDateTime> recur = new Recur<>(rrule);
            ZonedDateTime now  = ZonedDateTime.now(ZoneId.of(timezone));
            ZonedDateTime next = recur.getNextDate(now, now);
            if (next == null) {
                log.warn("RRULE '{}' produced no next date, defaulting to +1 day", rrule);
                return OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);
            }
            return next.toOffsetDateTime();
        } catch (Exception e) {
            log.warn("Failed to compute next generate from rrule='{}', timezone='{}': {}",
                    rrule, timezone, e.getMessage());
            return OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);
        }
    }
}
