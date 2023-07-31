package com.controller;

import com.service.ArticleService;
import com.vo.Result;
import com.vo.params.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /*
    * 分页查询（包括模糊查询 （文章名字、作者名字、文章类别、标签）查询）
    * */
    @PostMapping("/articleList")
    public Result articleList(@RequestBody PageParam pageParam){
        return articleService.articleList(pageParam);
    }
    /*
    * 删除
    * */
    @DeleteMapping("/deleteArticle/{id}")
    public Result deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }
}
