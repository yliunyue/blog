package com.service;

import com.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.PageParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
public interface QuestionService extends IService<Question> {

    Result findQuestion(PageParam pageParams);

    Result deleteQuestion(Long id);
}
