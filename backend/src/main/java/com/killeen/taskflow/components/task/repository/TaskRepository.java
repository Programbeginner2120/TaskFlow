package com.killeen.taskflow.components.task.repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.analytics.constants.DashboardAnalyticsConstants.TaskDataDuration;
import com.killeen.taskflow.components.analytics.constants.DashboardAnalyticsConstants.TaskDataStatus;
import com.killeen.taskflow.components.analytics.model.DashboardAnalyticsRequest;
import com.killeen.taskflow.components.task.converter.TaskConverter;
import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.db.mapper.generated.TaskDbMapper;
import com.killeen.taskflow.db.model.generated.TaskDb;
import com.killeen.taskflow.db.model.generated.TaskDbExample;
import com.killeen.taskflow.db.model.generated.TaskDbExample.Criteria;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final TaskDbMapper      taskDbMapper;
    private final TaskConverter     taskConverter;
    private final SubtaskRepository subtaskRepository;

    public List<Task> findAllByUserId(Long userId) {
        TaskDbExample example = new TaskDbExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("created_at DESC");
        List<TaskDb> taskDbs = taskDbMapper.selectByExample(example);

        if (taskDbs.isEmpty()) {
            return List.of();
        }

        List<Long> taskIds = taskDbs.stream().map(TaskDb::getId).toList();
        Map<Long, List<Subtask>> subtasksByTaskId = subtaskRepository
                .findAllByTaskIdIn(taskIds).stream()
                .collect(Collectors.groupingBy(Subtask::getTaskId));

        return taskDbs.stream()
                .map(taskDb -> {
                    Task task = taskConverter.toDto(taskDb);
                    task.setSubtasks(subtasksByTaskId.getOrDefault(task.getId(), List.of()));
                    return task;
                })
                .toList();
    }

    public Optional<Task> findByIdAndUserId(Long id, Long userId) {
        TaskDbExample example = new TaskDbExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andUserIdEqualTo(userId);
        return taskDbMapper.selectByExample(example).stream()
                .findFirst()
                .map(taskDb -> {
                    Task task = taskConverter.toDto(taskDb);
                    task.setSubtasks(subtaskRepository.findAllByTaskId(task.getId()));
                    return task;
                });
    }

    public Optional<List<Task>> findByTaskListId(Long taskListId) {
        TaskDbExample example = new TaskDbExample();
        example.createCriteria()
                .andListIdEqualTo(taskListId);
        return Optional.of(taskDbMapper.selectByExample(example).stream()
            .map(taskDb -> {
                Task task = taskConverter.toDto(taskDb);
                task.setSubtasks(subtaskRepository.findAllByTaskId(task.getId()));
                return task;
            })
            .toList()
        );
    }

    public Optional<List<Task>> retrieveDashboardAnalyticsTaskData(Long userId, DashboardAnalyticsRequest dashboardAnalyticsRequest) {

        TaskDataDuration taskDataDuration = dashboardAnalyticsRequest.getDurationSelection();
        Integer createdAtDayOffset;
        switch(taskDataDuration) {
                case LAST_7_DAYS:
                        createdAtDayOffset = 7;
                        break;
                case LAST_30_DAYS:
                        createdAtDayOffset = 30;
                        break;
                case LAST_90_DAYS:
                        createdAtDayOffset = 90;
                        break;
                default:
                        createdAtDayOffset = null;
                        break;
        }

        OffsetDateTime createdAtParameter = createdAtDayOffset != null 
                ? OffsetDateTime.now(ZoneOffset.UTC).minusDays(createdAtDayOffset)
                : OffsetDateTime.MIN;

        TaskDbExample example = new TaskDbExample();
        Criteria criteria = example.createCriteria().andUserIdEqualTo(userId)
                .andCreatedAtGreaterThanOrEqualTo(createdAtParameter);

        List<Long> listSelections = dashboardAnalyticsRequest.getListSelections();
        if (listSelections != null && !listSelections.isEmpty()) {
                criteria = criteria.andListIdIn(listSelections);
        }

        TaskDataStatus taskDataStatus = dashboardAnalyticsRequest.getStatusSelection();
        if (TaskDataStatus.ACTIVE.equals(taskDataStatus)) {
                criteria = criteria.andCompletedAtIsNull();
        } else if (TaskDataStatus.COMPLETED.equals(taskDataStatus)) {
                criteria = criteria.andCompletedAtIsNotNull();
        }

        example.setOrderByClause("created_at DESC");

        List<TaskDb> taskDbs = this.taskDbMapper.selectByExample(example);

        List<Long> taskIds = taskDbs.stream().map(TaskDb::getId).toList();
        Map<Long, List<Subtask>> subtasksByTaskId = subtaskRepository
                .findAllByTaskIdIn(taskIds).stream()
                .collect(Collectors.groupingBy(Subtask::getTaskId));

        return Optional.of(taskDbs
            .stream()
            .map(taskDb -> {
                Task task = taskConverter.toDto(taskDb);
                task.setSubtasks(subtasksByTaskId.getOrDefault(task.getId(), List.of()));
                return task;
            })
            .toList());

    }

    public Long save(Task task) {
        TaskDb dbModel = taskConverter.toDb(task);
        taskDbMapper.insert(dbModel);
        return dbModel.getId();
    }

    public void update(Task task) {
        TaskDb dbModel = taskConverter.toDb(task);
        taskDbMapper.updateByPrimaryKey(dbModel);
    }

    public void deleteById(Long id) {
        taskDbMapper.deleteByPrimaryKey(id);
    }
}
