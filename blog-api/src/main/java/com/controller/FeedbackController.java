package com.controller;

import com.service.FeedbackService;
import com.vo.Result;
import com.vo.params.AnswerParam;
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
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/feedbackList")
    public Result feedbackList(){
        return feedbackService.feedbackList();
    }

    @PostMapping()
    public Result feedback(@RequestBody AnswerParam answerParam){
        return feedbackService.feedback(answerParam);
    }
}
