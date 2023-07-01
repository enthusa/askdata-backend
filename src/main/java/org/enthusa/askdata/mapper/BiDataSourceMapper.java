package org.enthusa.askdata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.enthusa.askdata.entity.BiDataSource;
import org.enthusa.avatar.mybatis.mapper.SqlMapper;

@Mapper
public interface BiDataSourceMapper extends SqlMapper<BiDataSource, Integer> {
    int insertSelective(BiDataSource record);

    int updateByPrimaryKeySelective(BiDataSource record);
}
