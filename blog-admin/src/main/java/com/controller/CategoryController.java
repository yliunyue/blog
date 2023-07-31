package com.controller;

import com.entity.Category;
import com.service.CategoryService;
import com.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
    * 查询分类列表（可根据分类名字模糊查询）
    * */
    @GetMapping("/categoryList")
    public Result categoryList(@RequestParam(value = "categoryName",required = false) String categoryName) {
        return categoryService.categoryList(categoryName);
    }
    /*
    * 添加类别
    * */
    @PostMapping("/addCategory")
    public Result addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping("/updataCategory")
    public Result updataCategory(@RequestBody Category category){
        return categoryService.updataCategory(category);
    }

    /*
    * 删除类别，文章包含了改类别的需要将文章类别id置为空
    * */
    @DeleteMapping("/deleteCategory/{id}")
    public Result deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);
    }

}
