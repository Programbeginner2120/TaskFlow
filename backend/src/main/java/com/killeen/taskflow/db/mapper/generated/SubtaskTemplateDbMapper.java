package com.killeen.taskflow.db.mapper.generated;

import com.killeen.taskflow.db.model.generated.SubtaskTemplateDb;
import com.killeen.taskflow.db.model.generated.SubtaskTemplateDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubtaskTemplateDbMapper {
    long countByExample(SubtaskTemplateDbExample example);

    int deleteByExample(SubtaskTemplateDbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SubtaskTemplateDb row);

    int insertSelective(SubtaskTemplateDb row);

    List<SubtaskTemplateDb> selectByExample(SubtaskTemplateDbExample example);

    SubtaskTemplateDb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SubtaskTemplateDb row, @Param("example") SubtaskTemplateDbExample example);

    int updateByExample(@Param("row") SubtaskTemplateDb row, @Param("example") SubtaskTemplateDbExample example);

    int updateByPrimaryKeySelective(SubtaskTemplateDb row);

    int updateByPrimaryKey(SubtaskTemplateDb row);
}