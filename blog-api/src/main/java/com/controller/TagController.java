package com.controller;

import com.service.TagService;
import com.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-06
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;
    @GetMapping("/hot")
    public Result hotTag(){
        //查询最热的6个标签
        int limit=6;
        return tagService.hotTag(limit);
    }

    /*
    * 查询所有标签
    * */
    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }
    /*
    * 查询所有标签 详情
    * */
    @GetMapping("/detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }
    /*
    * 根据标签id查询标签
    * */
    @GetMapping("/detail/{id}")
    public Result findDetailById(@PathVariable Long id){
        return tagService.findDetailById(id);
    }
}
