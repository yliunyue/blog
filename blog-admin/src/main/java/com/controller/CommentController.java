package com.controller;

import com.service.CommentService;
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
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /*
     *评论列表
     * */
    @GetMapping("/article/{id}")
    public Result commentByArticleId(@PathVariable("id") Long id){
        return  commentService.commentByArticleId(id);
    }
    /*
    * 删除id
    * */
    @DeleteMapping("/deleteComment/{id}")
    public Result deleteComment(@PathVariable("id") Long id){
        return  commentService.deleteComment(id);
    }

    /*
    * 分页查询全部评论
    * */
    @PostMapping("/pageComment")
    public Result pageComment(@RequestBody PageParam pageParam){
        return  commentService.pageComment(pageParam);
    }
}
