package com.killeen.taskflow.db.mapper.generated;

import com.killeen.taskflow.db.model.generated.TaskListDb;
import com.killeen.taskflow.db.model.generated.TaskListDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskListDbMapper {
    long countByExample(TaskListDbExample example);

    int deleteByExample(TaskListDbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TaskListDb row);

    int insertSelective(TaskListDb row);

    List<TaskListDb> selectByExample(TaskListDbExample example);

    TaskListDb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") TaskListDb row, @Param("example") TaskListDbExample example);

    int updateByExample(@Param("row") TaskListDb row, @Param("example") TaskListDbExample example);

    int updateByPrimaryKeySelective(TaskListDb row);

    int updateByPrimaryKey(TaskListDb row);
}