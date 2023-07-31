package com.controller;

import com.service.QuestionService;
import com.vo.Result;
import com.vo.params.AnswerParam;
import com.vo.params.AskQuestionParam;
import com.vo.params.PageSearchParam;
import com.vo.params.QuestionPageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    /*
    * 查询问题列表
    * */
    @PostMapping("/pageQuestion")
    public Result pageQuestion(@RequestBody QuestionPageParams pageParams){
        return questionService.pageQuestion(pageParams);
    }

    @PostMapping("/findQuestion")
    public Result findQuestion(@RequestBody PageSearchParam pageParams){
        return questionService.findQuestion(pageParams);
    }

    /*
    * System.currentTimeMillis()
    * 提问
    * */
    @PostMapping("/ask")
    public Result askQuestion(@RequestBody AskQuestionParam askQuestionParam){

        return questionService.askQuestion(askQuestionParam);
    }

    /*
    * 回答问题
    * */
    @PostMapping("/answer")
    public Result answerQuestion(@RequestBody AnswerParam answerParam,Integer questionId){

        return questionService.answerQuestion(answerParam,questionId);
    }

    /*
    * 确认问题已经解决
    * */
    @PostMapping("/status")
    public Result uploadStatus(Integer questionId){
        return questionService.uploadStatus(questionId);
    }
}
