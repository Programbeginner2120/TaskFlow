package com.killeen.taskflow.components.tasklisttemplate.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.tasklisttemplate.converter.TaskListTemplateConverter;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskListTemplate;
import com.killeen.taskflow.db.mapper.custom.TaskListTemplateDbCustomMapper;
import com.killeen.taskflow.db.mapper.generated.TaskListTemplateDbMapper;
import com.killeen.taskflow.db.model.generated.TaskListTemplateDb;
import com.killeen.taskflow.db.model.generated.TaskListTemplateDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskListTemplateRepository {

    private final TaskListTemplateDbMapper       taskListTemplateDbMapper;
    private final TaskListTemplateDbCustomMapper taskListTemplateDbCustomMapper;
    private final TaskListTemplateConverter      taskListTemplateConverter;

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

    /**
     * Attempts to acquire an exclusive row-level lock on a specific template
     * whose {@code next_generate} still matches the expected value.
     *
     * <p>{@code FOR UPDATE SKIP LOCKED} ensures that a concurrent caller already
     * holding the lock (or one arriving after {@code updateSchedule} has advanced
     * {@code next_generate}) will receive {@code false} instead of blocking or
     * re-processing the template.
     *
     * <p>Must be called inside an active {@code @Transactional} method so the lock
     * is held for the entire generation unit of work.
     *
     * @return {@code true} if the lock was acquired, {@code false} if the row was
     *         already locked or its {@code next_generate} has been advanced.
     */
    public boolean claimIfStillDue(Long id, OffsetDateTime nextGenerate) {
        return taskListTemplateDbCustomMapper
                .findByIdAndNextGenerateForUpdateSkipLocked(id, nextGenerate) != null;
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
