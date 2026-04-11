package com.killeen.taskflow.db.mapper.generated;

import com.killeen.taskflow.db.model.generated.TaskDb;
import com.killeen.taskflow.db.model.generated.TaskDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskDbMapper {
    long countByExample(TaskDbExample example);

    int deleteByExample(TaskDbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TaskDb row);

    int insertSelective(TaskDb row);

    List<TaskDb> selectByExample(TaskDbExample example);

    TaskDb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") TaskDb row, @Param("example") TaskDbExample example);

    int updateByExample(@Param("row") TaskDb row, @Param("example") TaskDbExample example);

    int updateByPrimaryKeySelective(TaskDb row);

    int updateByPrimaryKey(TaskDb row);
}