package org.enthusa.askdata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.enthusa.askdata.entity.BiTable;
import org.enthusa.avatar.mybatis.mapper.SqlMapper;

import java.util.List;

@Mapper
public interface BiTableMapper extends SqlMapper<BiTable, Integer> {
    BiTable selectByDsIdAndCatalogAndName(@Param("dsId") Integer dsId, @Param("catalog") String catalog, @Param("name") String name);

    int insertSelective(BiTable record);

    int updateByPrimaryKeySelective(BiTable record);

    int batchUpdateStatusByPrimaryKeys(@Param("list") List<Integer> ids);

    int batchUpdateHotValue(@Param("list") List<BiTable> tables);
}
