package org.enthusa.askdata.dao.impl;

import org.enthusa.askdata.dao.BiTableDao;
import org.enthusa.askdata.entity.BiTable;
import org.enthusa.askdata.mapper.BiTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author henry
 * @date 2022/3/18
 */
@Repository("biTableDao")
public class BiTableDaoImpl implements BiTableDao {
    @Autowired(required = false)
    private BiTableMapper biTableMapper;

    @Override
    public Integer save(BiTable table) {
        BiTable biTable = biTableMapper.selectByDsIdAndCatalogAndName(table.getDsId(), table.getCatalog(), table.getName());
        if (Objects.isNull(biTable)) {
            biTable = new BiTable();
            biTable.setDsId(table.getDsId());
            biTable.setCatalog(table.getCatalog());
            biTable.setName(table.getName());
        }
        biTable.setStatus(table.getStatus());
        biTable.setBrief(table.getBrief());
        if (Objects.isNull(biTable.getUpdateUserId()) || Objects.nonNull(table.getUpdateUserId())) {
            biTable.setRemarks(table.getRemarks());
        }
        biTable.setOwnerId(table.getOwnerId());
        biTable.setUpdateUserId(table.getUpdateUserId());
        if (Objects.isNull(biTable.getId())) {
            biTableMapper.insertSelective(biTable);
        } else {
            biTableMapper.updateByPrimaryKeySelective(biTable);
        }
        return biTable.getId();
    }
}
