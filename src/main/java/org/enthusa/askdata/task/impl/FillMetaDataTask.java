package org.enthusa.askdata.task.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.enthusa.askdata.dao.BiFieldDao;
import org.enthusa.askdata.dao.BiTableDao;
import org.enthusa.askdata.entity.BiDataSource;
import org.enthusa.askdata.entity.BiField;
import org.enthusa.askdata.entity.BiTable;
import org.enthusa.askdata.mapper.BiDataSourceMapper;
import org.enthusa.askdata.mapper.BiFieldMapper;
import org.enthusa.askdata.mapper.BiTableMapper;
import org.enthusa.askdata.task.AbstractTask;
import org.enthusa.avatar.core.consts.TextConstant;
import org.enthusa.avatar.db.metadata.ColumnEntity;
import org.enthusa.avatar.db.metadata.MetaDataUtils;
import org.enthusa.avatar.db.metadata.TableEntity;
import org.enthusa.avatar.utils.task.TaskModel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author henry
 * @date 2022/3/21
 */
@Slf4j
@Component
public class FillMetaDataTask extends AbstractTask {
    @Resource
    private BiDataSourceMapper biDataSourceMapper;

    @Resource
    private BiTableDao biTableDao;

    @Resource
    private BiFieldDao biFieldDao;

    @Resource
    private BiTableMapper biTableMapper;

    @Resource
    private BiFieldMapper biFieldMapper;

    @Override
    public void run(TaskModel taskModel) {
        List<Integer> biTablesBefore = biTableMapper.selectAll().stream().filter(t -> t.getStatus() == 0).map(BiTable::getId).collect(Collectors.toList());
        Set<Integer> biTablesAfter = new HashSet<>();
        List<Integer> biFieldsBefore = biFieldMapper.selectAll().stream().filter(t -> t.getStatus() == 0).map(BiField::getId).collect(Collectors.toList());
        Set<Integer> biFieldsAfter = new HashSet<>();
        List<BiDataSource> dsList = biDataSourceMapper.selectAll();
        for (BiDataSource ds : dsList) {
            byte[] bytes = Base64.getDecoder().decode(ds.getDetails());
            Properties info = JSON.parseObject(new String(bytes), Properties.class);
            List<String> catalogs = TextConstant.COMMA_SPLITTER.splitToList(ds.getCatalogs());
            try (Connection conn = DriverManager.getConnection(info.getProperty("url"), info)) {
                DatabaseMetaData metaData = conn.getMetaData();
                for (String catalog : catalogs) {
                    List<TableEntity> tables = MetaDataUtils.getTables(metaData, catalog);
                    for (TableEntity table : tables) {
                        if (StringUtils.containsAny(table.getName(), "tmp", "temp", "bak", "backup")
                                && !StringUtils.startsWithAny(table.getName(), "ods", "dwd", "dwm", "dws", "dim", "app")) {
                            log.warn("{}, skipped!", table);
                            continue;
                        }
                        log.info("{}", table);
                        BiTable biTable = new BiTable();
                        biTable.setDsId(ds.getId());
                        biTable.setCatalog(table.getCatalog());
                        biTable.setName(table.getName());
                        biTable.setRemarks(table.getRemarks());
                        biTable.setStatus(0);
                        Integer tableId = biTableDao.save(biTable);
                        biTablesAfter.add(tableId);
                        List<ColumnEntity> columns = MetaDataUtils.getColumns(metaData, catalog, table.getName());
                        for (ColumnEntity column : columns) {
                            BiField biField = new BiField();
                            biField.setTableId(tableId);
                            biField.setColumnSeq(column.getColumnSeq());
                            biField.setPrimaryKeySeq(column.getPrimaryKeySeq());
                            biField.setName(column.getName());
                            biField.setRemarks(column.getRemarks());
                            biField.setTypeName(column.getTypeName());
                            biField.setColumnSize(column.getColumnSize());
                            biField.setIsAutoIncrement(column.getIsAutoIncrement());
                            biField.setIsNullable(column.getIsNullable());
                            biField.setDefaultValue(column.getDefaultValue());
                            biField.setStatus(0);
                            Integer fieldId = biFieldDao.save(biField);
                            biFieldsAfter.add(fieldId);
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        List<Integer> biTables = biTablesBefore.stream().filter(t -> !biTablesAfter.contains(t)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(biTables)) {
            biTableMapper.batchUpdateStatusByPrimaryKeys(biTables);
        }
        List<Integer> biFields = biFieldsBefore.stream().filter(t -> !biFieldsAfter.contains(t)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(biFields)) {
            biFieldMapper.batchUpdateStatusByPrimaryKeys(biFields);
        }
    }
}
