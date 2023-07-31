package com.controller;

import com.service.QuestionService;
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
 * @since 2023-04-13
 */
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping("/findQuestion")
    public Result findQuestion(@RequestBody PageParam pageParams){
        return questionService.findQuestion(pageParams);
    }

    @PostMapping("/deleteQuestion/{id}")
    public Result deleteQuestion(@PathVariable("id") Long id){
        return questionService.deleteQuestion(id);
    }
}
