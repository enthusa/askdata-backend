package org.enthusa.askdata.controller;

import org.enthusa.askdata.entity.BiTable;
import org.enthusa.askdata.mapper.BiTableMapper;
import org.enthusa.avatar.face.type.PageModel;
import org.enthusa.avatar.face.type.Result;
import org.enthusa.avatar.face.utils.ResultUtil;
import org.enthusa.avatar.face.utils.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bi")
public class BiTableController {
    @Resource
    private BiTableMapper biTableMapper;

    @GetMapping("/tables")
    public Result index(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize
    ) {
        PageModel<BiTable> pageModel = new PageModel<>();
        pageModel.setPageAndPageSize(page, pageSize);
        List<BiTable> tableList = biTableMapper.selectByPage(pageModel);
        pageModel.setList(tableList);
        return ResultUtil.success(pageModel);
    }

    @GetMapping("/tables/{id}")
    public Result show(@PathVariable("id") Integer id) {
        Validate.idValid("id", id);
        BiTable table = biTableMapper.selectByPrimaryKey(id);
        Validate.hasRecord("id", id, table);
        return ResultUtil.success(table);
    }

    @PostMapping("/tables")
    public Result create(@RequestBody @Valid BiTable biTable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Validate.isRecord(true, bindingResult.getFieldError().getDefaultMessage());
        }

        BiTable table = new BiTable();
        BeanUtils.copyProperties(biTable, table);
        biTableMapper.insertSelective(table);
        return ResultUtil.success(table);
    }

    @PutMapping("/tables/{id}")
    public Result update(@PathVariable("id") Integer id, @RequestBody @Valid BiTable biTable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Validate.isRecord(true, bindingResult.getFieldError().getDefaultMessage());
        }

        Validate.idValid("id", id);
        BiTable table = biTableMapper.selectByPrimaryKey(id);
        Validate.hasRecord("id", id, table);

        BeanUtils.copyProperties(biTable, table);
        biTableMapper.updateByPrimaryKey(table);
        return ResultUtil.success(table);
    }
}
