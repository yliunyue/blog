package com.service.impl;

import com.entity.Adminfeed;
import com.mapper.AdminfeedMapper;
import com.service.AdminfeedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.params.AdminFeedParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
@Service
public class AdminfeedServiceImpl extends ServiceImpl<AdminfeedMapper, Adminfeed> implements AdminfeedService {
    @Autowired
    private AdminfeedMapper adminfeedMapper;
    @Override
    @Transactional
    public Result adminFeed(AdminFeedParam adminFeedParam) {
        Adminfeed adminfeed = new Adminfeed();
        adminfeed.setAdminName("系统管理员");
        adminfeed.setContent(adminFeedParam.getContent());
        adminfeed.setCreateDate(LocalDateTime.now());
        adminfeed.setFeedbackId(adminFeedParam.getId());
        int insert = adminfeedMapper.insert(adminfeed);
        if (insert > 0) {
            return Result.success("回复成功");
        }
        return null;
    }
}
