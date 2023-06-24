package org.enthusa.askdata.controller;

import org.enthusa.askdata.entity.BiPost;
import org.enthusa.askdata.mapper.BiPostMapper;
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
@RequestMapping("/api")
public class BiPostController {
    @Resource
    private BiPostMapper biPostMapper;

    @GetMapping("/posts")
    public Result index(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize
    ) {
        PageModel<BiPost> pageModel = new PageModel<>();
        pageModel.setPageAndPageSize(page, pageSize);
        List<BiPost> postList = biPostMapper.selectByPage(pageModel);
        pageModel.setList(postList);
        return ResultUtil.success(pageModel);
    }

    @GetMapping("/posts/{id}")
    public Result show(@PathVariable("id") Integer id) {
        Validate.idValid("id", id);
        BiPost post = biPostMapper.selectByPrimaryKey(id);
        Validate.hasRecord("id", id, post);
        return ResultUtil.success(post);
    }

    @PostMapping("/posts")
    public Result create(@RequestBody @Valid BiPost biPost, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Validate.isRecord(true, bindingResult.getFieldError().getDefaultMessage());
        }

        BiPost post = new BiPost();
        BeanUtils.copyProperties(biPost, post);
        biPostMapper.insertSelective(post);
        return ResultUtil.success(post);
    }

    @PutMapping("/posts/{id}")
    public Result update(@PathVariable("id") Integer id, @RequestBody @Valid BiPost biPost, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Validate.isRecord(true, bindingResult.getFieldError().getDefaultMessage());
        }

        Validate.idValid("id", id);
        BiPost post = biPostMapper.selectByPrimaryKey(id);
        Validate.hasRecord("id", id, post);

        BeanUtils.copyProperties(biPost, post);
        biPostMapper.updateByPrimaryKey(post);
        return ResultUtil.success(post);
    }
}
