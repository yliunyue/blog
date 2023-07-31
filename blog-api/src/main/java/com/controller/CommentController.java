package com.controller;

import com.service.CommentService;
import com.vo.Result;
import com.vo.params.CommentParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-08
 */
@RestController
@Slf4j
@RequestMapping("/comments")
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
    *评论
    * */
    @PostMapping("/create/change")
    public Result comments(@RequestBody CommentParam commentParam){
        log.info("拦截到的评论内容为：{}",commentParam.toString());
        return  commentService.comments(commentParam);
    }



}
