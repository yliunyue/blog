package com.service;

import com.entity.Adminfeed;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.AdminFeedParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
public interface AdminfeedService extends IService<Adminfeed> {

    Result adminFeed(AdminFeedParam adminFeedParam);
}
