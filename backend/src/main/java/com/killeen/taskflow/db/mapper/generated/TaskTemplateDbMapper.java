package com.killeen.taskflow.db.mapper.generated;

import com.killeen.taskflow.db.model.generated.TaskTemplateDb;
import com.killeen.taskflow.db.model.generated.TaskTemplateDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskTemplateDbMapper {
    long countByExample(TaskTemplateDbExample example);

    int deleteByExample(TaskTemplateDbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TaskTemplateDb row);

    int insertSelective(TaskTemplateDb row);

    List<TaskTemplateDb> selectByExample(TaskTemplateDbExample example);

    TaskTemplateDb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") TaskTemplateDb row, @Param("example") TaskTemplateDbExample example);

    int updateByExample(@Param("row") TaskTemplateDb row, @Param("example") TaskTemplateDbExample example);

    int updateByPrimaryKeySelective(TaskTemplateDb row);

    int updateByPrimaryKey(TaskTemplateDb row);
}