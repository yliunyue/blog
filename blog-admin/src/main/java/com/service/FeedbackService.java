package com.service;

import com.entity.Feedback;
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
public interface FeedbackService extends IService<Feedback> {

    Result pageFeedBack(PageParam pageParam);
}
