package com.controller;

import com.entity.Feedback;
import com.service.FeedbackService;
import com.vo.Result;
import com.vo.params.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/pageFeedBack")
    public Result pageFeedBack(@RequestBody PageParam pageParam){
        return feedbackService.pageFeedBack(pageParam);
    }
}
