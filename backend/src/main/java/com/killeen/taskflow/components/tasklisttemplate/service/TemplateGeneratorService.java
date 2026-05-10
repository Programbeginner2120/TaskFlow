package com.killeen.taskflow.components.tasklisttemplate.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import com.killeen.taskflow.components.task.TaskEncryptionHelper;
import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.components.task.repository.SubtaskRepository;
import com.killeen.taskflow.components.task.repository.TaskRepository;
import com.killeen.taskflow.components.tasklist.TaskListEncryptionHelper;
import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.components.tasklist.repository.TaskListRepository;
import com.killeen.taskflow.components.tasklisttemplate.model.SubtaskTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplate;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskTemplate;
import com.killeen.taskflow.components.tasklisttemplate.repository.TaskListTemplateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateGeneratorService {

    private final TaskListRepository           taskListRepository;
    private final TaskRepository               taskRepository;
    private final SubtaskRepository            subtaskRepository;
    private final TaskListEncryptionHelper     taskListEncryptionHelper;
    private final TaskEncryptionHelper         taskEncryptionHelper;
    private final TaskListTemplateRepository   templateRepository;
    private final RruleService                 rruleService;

    @Transactional
    public void generateFromTemplate(TaskListTemplate template) {
        if (!templateRepository.claimIfStillDue(template.getId(), template.getNextGenerate())) {
            log.info("Template {} already claimed by another instance, skipping", template.getId());
            return;
        }

        OffsetDateTime now   = OffsetDateTime.now(ZoneOffset.UTC);
        LocalDate      today = now.atZoneSameInstant(ZoneId.of(template.getTimezone())).toLocalDate();

        String listName = (template.getGenerationTitle() != null && !template.getGenerationTitle().isBlank())
                ? resolveGenerationTitle(template.getGenerationTitle(), today)
                : template.getName();

        TaskList taskList = TaskList.builder()
                .userId(template.getUserId())
                .name(listName)
                .color(template.getColor())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long listId = taskListRepository.save(taskListEncryptionHelper.encrypt(taskList));

        for (int i = 0; i < template.getTaskTemplates().size(); i++) {
                TaskTemplate taskTemplate = template.getTaskTemplates().get(i);
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
                        .position((long) i)
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

        OffsetDateTime nextGenerate = rruleService.computeNextGenerate(
                template.getRrule(), template.getTimezone());
        templateRepository.updateSchedule(template.getId(), now, nextGenerate);

        log.info("Generated task list {} from template {} for user {}",
                listId, template.getId(), template.getUserId());
    }

        private String resolveGenerationTitle(String pattern, LocalDate date) {
                if (pattern == null) return null;
                String result = pattern;
                result = result.replace("YYYY", String.format("%04d", date.getYear()));
                result = result.replace("MM", String.format("%02d", date.getMonthValue()));
                result = result.replace("DD", String.format("%02d", date.getDayOfMonth()));
                return result;
        }
}
