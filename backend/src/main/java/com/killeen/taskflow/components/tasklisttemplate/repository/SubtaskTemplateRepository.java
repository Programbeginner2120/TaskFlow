package com.killeen.taskflow.components.tasklisttemplate.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.tasklisttemplate.converter.SubtaskTemplateConverter;
import com.killeen.taskflow.components.tasklisttemplate.model.SubtaskTemplate;
import com.killeen.taskflow.db.mapper.generated.SubtaskTemplateDbMapper;
import com.killeen.taskflow.db.model.generated.SubtaskTemplateDb;
import com.killeen.taskflow.db.model.generated.SubtaskTemplateDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubtaskTemplateRepository {

    private final SubtaskTemplateDbMapper    subtaskTemplateDbMapper;
    private final SubtaskTemplateConverter   subtaskTemplateConverter;

    public List<SubtaskTemplate> findAllByTaskTemplateId(Long taskTemplateId) {
        SubtaskTemplateDbExample example = new SubtaskTemplateDbExample();
        example.createCriteria().andTaskTemplateIdEqualTo(taskTemplateId);
        example.setOrderByClause("created_at ASC");
        return subtaskTemplateDbMapper.selectByExample(example).stream()
                .map(subtaskTemplateConverter::toDto)
                .toList();
    }

    public List<SubtaskTemplate> findAllByTaskTemplateIdIn(List<Long> taskTemplateIds) {
        if (taskTemplateIds.isEmpty()) {
            return List.of();
        }
        SubtaskTemplateDbExample example = new SubtaskTemplateDbExample();
        example.createCriteria().andTaskTemplateIdIn(taskTemplateIds);
        example.setOrderByClause("task_template_id ASC, created_at ASC");
        return subtaskTemplateDbMapper.selectByExample(example).stream()
                .map(subtaskTemplateConverter::toDto)
                .toList();
    }

    public Long save(SubtaskTemplate subtaskTemplate) {
        SubtaskTemplateDb dbModel = subtaskTemplateConverter.toDb(subtaskTemplate);
        subtaskTemplateDbMapper.insert(dbModel);
        return dbModel.getId();
    }

    public void deleteById(Long id) {
        subtaskTemplateDbMapper.deleteByPrimaryKey(id);
    }

    public void deleteAllByTaskTemplateId(Long taskTemplateId) {
        SubtaskTemplateDbExample example = new SubtaskTemplateDbExample();
        example.createCriteria().andTaskTemplateIdEqualTo(taskTemplateId);
        subtaskTemplateDbMapper.deleteByExample(example);
    }
}
