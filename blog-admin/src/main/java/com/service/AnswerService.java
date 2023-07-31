package com.service;

import com.entity.Answer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.AnswerVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
public interface AnswerService extends IService<Answer> {

    List<AnswerVo> findAnswerById(Integer id);
}
