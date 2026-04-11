package com.killeen.taskflow.db.mapper.generated;

import com.killeen.taskflow.db.model.generated.SubtaskDb;
import com.killeen.taskflow.db.model.generated.SubtaskDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubtaskDbMapper {
    long countByExample(SubtaskDbExample example);

    int deleteByExample(SubtaskDbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SubtaskDb row);

    int insertSelective(SubtaskDb row);

    List<SubtaskDb> selectByExample(SubtaskDbExample example);

    SubtaskDb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SubtaskDb row, @Param("example") SubtaskDbExample example);

    int updateByExample(@Param("row") SubtaskDb row, @Param("example") SubtaskDbExample example);

    int updateByPrimaryKeySelective(SubtaskDb row);

    int updateByPrimaryKey(SubtaskDb row);
}