package com.killeen.taskflow.components.task.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.task.converter.TaskConverter;
import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.db.mapper.generated.TaskDbMapper;
import com.killeen.taskflow.db.model.generated.TaskDb;
import com.killeen.taskflow.db.model.generated.TaskDbExample;

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
