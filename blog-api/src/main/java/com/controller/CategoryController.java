package com.controller;

import com.mapper.CategoryMapper;
import com.service.CategoryService;
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
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    //获取文章类别
    public Result categorys(){
        return categoryService.findAll();
    }

    /*
    * 查询所有文章分类细节
    * */
    @GetMapping("/detail")
    public Result categoryDetail(){
        return categoryService.findAllDetail();
    }

    /*
    * 根据类别id查询所有文章
    * */
    @GetMapping("/detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id){
        return categoryService.categoryDetailById(id);
    }


}
