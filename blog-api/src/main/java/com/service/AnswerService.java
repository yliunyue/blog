package com.service;

import com.pojo.Answer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.params.AnswerVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-03-31
 */
public interface AnswerService extends IService<Answer> {
    //根据问题答案查找id
    List<AnswerVo> findAnswerById(Integer id);
}
