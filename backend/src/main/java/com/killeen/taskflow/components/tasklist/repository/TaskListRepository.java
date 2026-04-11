package com.killeen.taskflow.components.tasklist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.tasklist.converter.TaskListConverter;
import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.db.mapper.generated.TaskListDbMapper;
import com.killeen.taskflow.db.model.generated.TaskListDb;
import com.killeen.taskflow.db.model.generated.TaskListDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskListRepository {

    private final TaskListDbMapper  taskListDbMapper;
    private final TaskListConverter taskListConverter;

    public List<TaskList> findAllByUserId(Long userId) {
        TaskListDbExample example = new TaskListDbExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("created_at ASC");
        return taskListDbMapper.selectByExample(example).stream()
                .map(taskListConverter::toDto)
                .toList();
    }

    public Optional<TaskList> findByIdAndUserId(Long id, Long userId) {
        TaskListDbExample example = new TaskListDbExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andUserIdEqualTo(userId);
        return taskListDbMapper.selectByExample(example).stream()
                .findFirst()
                .map(taskListConverter::toDto);
    }

    public Long save(TaskList taskList) {
        TaskListDb dbModel = taskListConverter.toDb(taskList);
        taskListDbMapper.insert(dbModel);
        return dbModel.getId();
    }

    public void update(TaskList taskList) {
        TaskListDb dbModel = taskListConverter.toDb(taskList);
        taskListDbMapper.updateByPrimaryKey(dbModel);
    }

    public void deleteById(Long id) {
        taskListDbMapper.deleteByPrimaryKey(id);
    }
}
