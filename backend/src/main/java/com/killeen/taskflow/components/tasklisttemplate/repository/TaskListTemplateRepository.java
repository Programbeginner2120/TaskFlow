package com.killeen.taskflow.components.tasklisttemplate.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.tasklisttemplate.converter.TaskListTemplateConverter;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplate;
import com.killeen.taskflow.db.mapper.generated.TaskListTemplateDbMapper;
import com.killeen.taskflow.db.model.generated.TaskListTemplateDb;
import com.killeen.taskflow.db.model.generated.TaskListTemplateDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskListTemplateRepository {

    private final TaskListTemplateDbMapper    taskListTemplateDbMapper;
    private final TaskListTemplateConverter   taskListTemplateConverter;

    public List<TaskListTemplate> findAllByUserId(Long userId) {
        TaskListTemplateDbExample example = new TaskListTemplateDbExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("created_at ASC");
        return taskListTemplateDbMapper.selectByExample(example).stream()
                .map(taskListTemplateConverter::toDto)
                .toList();
    }

    public Optional<TaskListTemplate> findByIdAndUserId(Long id, Long userId) {
        TaskListTemplateDbExample example = new TaskListTemplateDbExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andUserIdEqualTo(userId);
        return taskListTemplateDbMapper.selectByExample(example).stream()
                .findFirst()
                .map(taskListTemplateConverter::toDto);
    }

    public List<TaskListTemplate> findDueTemplates(OffsetDateTime asOf) {
        TaskListTemplateDbExample example = new TaskListTemplateDbExample();
        example.createCriteria().andNextGenerateLessThanOrEqualTo(asOf);
        return taskListTemplateDbMapper.selectByExample(example).stream()
                .map(taskListTemplateConverter::toDto)
                .toList();
    }

    public Long save(TaskListTemplate taskListTemplate) {
        TaskListTemplateDb dbModel = taskListTemplateConverter.toDb(taskListTemplate);
        taskListTemplateDbMapper.insert(dbModel);
        return dbModel.getId();
    }

    public void update(TaskListTemplate taskListTemplate) {
        TaskListTemplateDb dbModel = taskListTemplateConverter.toDb(taskListTemplate);
        taskListTemplateDbMapper.updateByPrimaryKey(dbModel);
    }

    public void updateSchedule(Long id, OffsetDateTime lastGenerated, OffsetDateTime nextGenerate) {
        TaskListTemplateDb update = new TaskListTemplateDb();
        update.setId(id);
        update.setLastGenerated(lastGenerated);
        update.setNextGenerate(nextGenerate);
        update.setUpdatedAt(lastGenerated);
        taskListTemplateDbMapper.updateByPrimaryKeySelective(update);
    }

    public void deleteById(Long id) {
        taskListTemplateDbMapper.deleteByPrimaryKey(id);
    }
}
