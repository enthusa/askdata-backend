package org.enthusa.askdata.controller;

import lombok.extern.slf4j.Slf4j;
import org.enthusa.askdata.entity.BiDataSource;
import org.enthusa.askdata.mapper.BiDataSourceMapper;
import org.enthusa.askdata.task.impl.FillMetaDataTask;
import org.enthusa.avatar.db.metadata.MetaDataUtils;
import org.enthusa.avatar.face.type.PageModel;
import org.enthusa.avatar.face.type.Result;
import org.enthusa.avatar.face.utils.ResultUtil;
import org.enthusa.avatar.face.utils.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/bi")
public class BiDataSourceController {
    @Autowired
    private BiDataSourceMapper biDataSourceMapper;

    @Autowired
    private FillMetaDataTask fillMetaDataTask;

    @GetMapping("/catalogs")
    public Result getAllCatalogs() {
        List<String> catalogs = biDataSourceMapper.selectAll().stream().flatMap(source -> {
            source.fillDerivedFieldsFromDatabase();
            return source.getCatalogList().stream();
        }).collect(Collectors.toList());
        return ResultUtil.success(catalogs);
    }

    @GetMapping("/datasources")
    public Result index(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize
    ) {
        PageModel<BiDataSource> pageModel = new PageModel<>();
        pageModel.setPageAndPageSize(page, pageSize);
        List<BiDataSource> dataSourceList = biDataSourceMapper.selectByPage(pageModel);
        pageModel.setList(dataSourceList);
        return ResultUtil.success(pageModel);
    }

    @GetMapping("/datasources/{id}")
    public Result show(Model model, @PathVariable("id") Integer id) throws IOException {
        Validate.idValid("id", id);
        BiDataSource dataSource = biDataSourceMapper.selectByPrimaryKey(id);
        Validate.hasRecord("id", id, dataSource);
        dataSource.fillDerivedFieldsFromDatabase();
        model.addAttribute("dataSource", dataSource);

        try (Connection conn = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUser(), dataSource.getPassword())) {
            List<String> catalogs = MetaDataUtils.getCatalogs(conn.getMetaData());
            model.addAttribute("catalogs", catalogs);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return ResultUtil.success(model);
    }

    @PostMapping("/datasources")
    public Result create(@RequestBody @Valid BiDataSource biDataSource, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Validate.isRecord(true, bindingResult.getFieldError().getDefaultMessage());
        }

        BiDataSource dataSource = new BiDataSource();
        BeanUtils.copyProperties(biDataSource, dataSource);
        dataSource.convertToDatabaseValue();
        biDataSourceMapper.insertSelective(dataSource);
        fillMetaDataTask.start();
        return ResultUtil.success(dataSource);
    }

    @PutMapping("/datasources/{id}")
    public Result update(@PathVariable("id") Integer id, @RequestBody @Valid BiDataSource biDataSource, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Validate.isRecord(true, bindingResult.getFieldError().getDefaultMessage());
        }

        Validate.idValid("id", id);
        BiDataSource dataSource = biDataSourceMapper.selectByPrimaryKey(id);
        Validate.hasRecord("id", id, dataSource);

        BeanUtils.copyProperties(biDataSource, dataSource);
        dataSource.convertToDatabaseValue();
        biDataSourceMapper.updateByPrimaryKeySelective(dataSource);
        fillMetaDataTask.start();
        return ResultUtil.success(dataSource);
    }
}
