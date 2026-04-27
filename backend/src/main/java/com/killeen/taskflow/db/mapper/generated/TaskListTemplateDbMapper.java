package com.killeen.taskflow.db.mapper.generated;

import com.killeen.taskflow.db.model.generated.TaskListTemplateDb;
import com.killeen.taskflow.db.model.generated.TaskListTemplateDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskListTemplateDbMapper {
    long countByExample(TaskListTemplateDbExample example);

    int deleteByExample(TaskListTemplateDbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TaskListTemplateDb row);

    int insertSelective(TaskListTemplateDb row);

    List<TaskListTemplateDb> selectByExample(TaskListTemplateDbExample example);

    TaskListTemplateDb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") TaskListTemplateDb row, @Param("example") TaskListTemplateDbExample example);

    int updateByExample(@Param("row") TaskListTemplateDb row, @Param("example") TaskListTemplateDbExample example);

    int updateByPrimaryKeySelective(TaskListTemplateDb row);

    int updateByPrimaryKey(TaskListTemplateDb row);
}