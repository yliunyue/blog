package com.service;

import com.pojo.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.AnswerParam;
import com.vo.params.AskQuestionParam;
import com.vo.params.PageSearchParam;
import com.vo.params.QuestionPageParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-03-31
 */
public interface QuestionService extends IService<Question> {

    /*
    * 查询问题列表
    * */
    Result pageQuestion(QuestionPageParams pageParams);

    /*
    * 提问
    * */
    Result askQuestion(AskQuestionParam askQuestionParam);

    /*
    * 回答问题
    * */
    Result answerQuestion(AnswerParam answerParam, Integer questionId);

    /*
    * 确认当前问题已解决
    * */
    Result uploadStatus(Integer questionId);

    Result findQuestion(PageSearchParam pageParams);

}
