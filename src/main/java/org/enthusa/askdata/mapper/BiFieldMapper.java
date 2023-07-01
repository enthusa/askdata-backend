package org.enthusa.askdata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.enthusa.askdata.entity.BiField;
import org.enthusa.avatar.mybatis.mapper.SqlMapper;

import java.util.List;

@Mapper
public interface BiFieldMapper extends SqlMapper<BiField, Integer> {
    List<BiField> selectByNameWithInfo(@Param("name") String name);

    List<Integer> selectTableIdsByName(@Param("name") String name);

    BiField selectByTableIdAndName(@Param("tableId") Integer tableId, @Param("name") String name);

    int insertSelective(BiField record);

    int updateByPrimaryKeySelective(BiField record);

    int batchUpdateStatusByPrimaryKeys(@Param("list") List<Integer> ids);

    List<BiField> selectByTableIds(@Param("list") List tableIds);

}
