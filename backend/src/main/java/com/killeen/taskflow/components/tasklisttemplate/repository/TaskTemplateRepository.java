package com.killeen.taskflow.components.tasklisttemplate.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.tasklisttemplate.converter.TaskTemplateConverter;
import com.killeen.taskflow.components.tasklisttemplate.model.TaskTemplate;
import com.killeen.taskflow.db.mapper.generated.TaskTemplateDbMapper;
import com.killeen.taskflow.db.model.generated.TaskTemplateDb;
import com.killeen.taskflow.db.model.generated.TaskTemplateDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskTemplateRepository {

    private final TaskTemplateDbMapper  taskTemplateDbMapper;
    private final TaskTemplateConverter taskTemplateConverter;

    public List<TaskTemplate> findAllByListTemplateId(Long listTemplateId) {
        TaskTemplateDbExample example = new TaskTemplateDbExample();
        example.createCriteria().andListTemplateIdEqualTo(listTemplateId);
        example.setOrderByClause("created_at ASC");
        return taskTemplateDbMapper.selectByExample(example).stream()
                .map(taskTemplateConverter::toDto)
                .toList();
    }

    public Long save(TaskTemplate taskTemplate) {
        TaskTemplateDb dbModel = taskTemplateConverter.toDb(taskTemplate);
        taskTemplateDbMapper.insert(dbModel);
        return dbModel.getId();
    }

    public void update(TaskTemplate taskTemplate) {
        TaskTemplateDb dbModel = taskTemplateConverter.toDb(taskTemplate);
        taskTemplateDbMapper.updateByPrimaryKey(dbModel);
    }

    public void deleteById(Long id) {
        taskTemplateDbMapper.deleteByPrimaryKey(id);
    }

    public void deleteAllByListTemplateId(Long listTemplateId) {
        TaskTemplateDbExample example = new TaskTemplateDbExample();
        example.createCriteria().andListTemplateIdEqualTo(listTemplateId);
        taskTemplateDbMapper.deleteByExample(example);
    }
}
