package org.enthusa.askdata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.enthusa.askdata.entity.BiPost;
import org.enthusa.avatar.mybatis.mapper.SqlMapper;

@Mapper
public interface BiPostMapper extends SqlMapper<BiPost, Integer> {
    int insertSelective(BiPost record);

    int updateByPrimaryKeySelective(BiPost record);
}
