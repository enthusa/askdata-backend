package org.enthusa.askdata.dao.impl;

import org.enthusa.askdata.dao.BiFieldDao;
import org.enthusa.askdata.entity.BiField;
import org.enthusa.askdata.mapper.BiFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author henry
 * @date 2022/3/19
 */
@Repository("biFieldDao")
public class BiFieldDaoImpl implements BiFieldDao {
    @Autowired(required = false)
    private BiFieldMapper biFieldMapper;

    @Override
    public Integer save(BiField field) {
        BiField biField = biFieldMapper.selectByTableIdAndName(field.getTableId(), field.getName());
        if (Objects.isNull(biField)) {
            biField = new BiField();
            biField.setTableId(field.getTableId());
            biField.setName(field.getName());
        }
        biField.setStatus(field.getStatus());
        biField.setColumnSeq(field.getColumnSeq());
        biField.setPrimaryKeySeq(field.getPrimaryKeySeq());
        biField.setReferId(field.getReferId());
        biField.setBrief(field.getBrief());
        if (Objects.isNull(biField.getUpdateUserId()) || Objects.nonNull(field.getUpdateUserId())) {
            biField.setRemarks(field.getRemarks());
        }
        biField.setTypeName(field.getTypeName());
        biField.setColumnSize(field.getColumnSize());
        biField.setIsAutoIncrement(field.getIsAutoIncrement());
        biField.setIsNullable(field.getIsNullable());
        biField.setDefaultValue(field.getDefaultValue());
        biField.setIsEnumerable(field.getIsEnumerable());
        biField.setOptions(field.getOptions());
        biField.setUpdateUserId(field.getUpdateUserId());
        if (Objects.isNull(biField.getId())) {
            biFieldMapper.insertSelective(biField);
        } else {
            biFieldMapper.updateByPrimaryKeySelective(biField);
        }
        return biField.getId();
    }
}
