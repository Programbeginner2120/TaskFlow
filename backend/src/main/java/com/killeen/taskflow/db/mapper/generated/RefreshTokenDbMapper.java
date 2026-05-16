package com.killeen.taskflow.db.mapper.generated;

import com.killeen.taskflow.db.model.generated.RefreshTokenDb;
import com.killeen.taskflow.db.model.generated.RefreshTokenDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RefreshTokenDbMapper {
    long countByExample(RefreshTokenDbExample example);

    int deleteByExample(RefreshTokenDbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RefreshTokenDb row);

    int insertSelective(RefreshTokenDb row);

    List<RefreshTokenDb> selectByExample(RefreshTokenDbExample example);

    RefreshTokenDb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") RefreshTokenDb row, @Param("example") RefreshTokenDbExample example);

    int updateByExample(@Param("row") RefreshTokenDb row, @Param("example") RefreshTokenDbExample example);

    int updateByPrimaryKeySelective(RefreshTokenDb row);

    int updateByPrimaryKey(RefreshTokenDb row);
}