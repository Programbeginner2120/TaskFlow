package com.killeen.taskflow.components.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.task.converter.SubtaskConverter;
import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.db.mapper.generated.SubtaskDbMapper;
import com.killeen.taskflow.db.model.generated.SubtaskDb;
import com.killeen.taskflow.db.model.generated.SubtaskDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubtaskRepository {

    private final SubtaskDbMapper  subtaskDbMapper;
    private final SubtaskConverter subtaskConverter;

    public List<Subtask> findAllByTaskId(Long taskId) {
        SubtaskDbExample example = new SubtaskDbExample();
        example.createCriteria().andTaskIdEqualTo(taskId);
        example.setOrderByClause("created_at ASC");
        return subtaskDbMapper.selectByExample(example).stream()
                .map(subtaskConverter::toDto)
                .toList();
    }

    public List<Subtask> findAllByTaskIdIn(List<Long> taskIds) {
        if (taskIds.isEmpty()) {
            return List.of();
        }
        SubtaskDbExample example = new SubtaskDbExample();
        example.createCriteria().andTaskIdIn(taskIds);
        example.setOrderByClause("task_id ASC, created_at ASC");
        return subtaskDbMapper.selectByExample(example).stream()
                .map(subtaskConverter::toDto)
                .toList();
    }

    public Optional<Subtask> findByIdAndTaskId(Long id, Long taskId) {
        SubtaskDbExample example = new SubtaskDbExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andTaskIdEqualTo(taskId);
        return subtaskDbMapper.selectByExample(example).stream()
                .findFirst()
                .map(subtaskConverter::toDto);
    }

    public Long save(Subtask subtask) {
        SubtaskDb dbModel = subtaskConverter.toDb(subtask);
        subtaskDbMapper.insert(dbModel);
        return dbModel.getId();
    }

    public void update(Subtask subtask) {
        SubtaskDb dbModel = subtaskConverter.toDb(subtask);
        subtaskDbMapper.updateByPrimaryKey(dbModel);
    }

    public void deleteById(Long id) {
        subtaskDbMapper.deleteByPrimaryKey(id);
    }
}
