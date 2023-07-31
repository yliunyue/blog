package com.service;

import com.entity.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.AnswerParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-03-31
 */
public interface FeedbackService extends IService<Feedback> {

    Result feedbackList();

    Result feedback(AnswerParam answerParam);
}
