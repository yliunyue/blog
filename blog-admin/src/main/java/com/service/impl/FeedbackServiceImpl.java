package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Adminfeed;
import com.entity.Feedback;
import com.entity.SysUser;
import com.mapper.AdminfeedMapper;
import com.mapper.FeedbackMapper;
import com.mapper.SysUserMapper;
import com.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.FeedBackVo;
import com.vo.params.PageParam;
import com.vo.params.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private AdminfeedMapper adminfeedMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Result pageFeedBack(PageParam pageParam) {
        //分页参数
        IPage<Feedback> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        //调用mp方法查询
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(pageParam.getQueryString()), Feedback::getContent, pageParam.getQueryString());
        IPage<Feedback> pageInfo = feedbackMapper.selectPage(page, queryWrapper);
        List<Feedback> records = pageInfo.getRecords();
        long total = pageInfo.getTotal();
        //封装数据未vo对象返回前端
        List<FeedBackVo> feedBackVoList = records.stream().map(new Function<Feedback, FeedBackVo>() {
            @Override
            public FeedBackVo apply(Feedback feedback) {
                FeedBackVo feedBackVo = new FeedBackVo();
                BeanUtils.copyProperties(feedback, feedBackVo);
                //查询管理员反馈
                LambdaQueryWrapper<Adminfeed> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(Adminfeed::getFeedbackId, feedback.getId()).last("limit 1");
                Adminfeed adminfeed = adminfeedMapper.selectOne(queryWrapper1);
                feedBackVo.setAdminfeed(adminfeed);
                //用户名
                LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
                sysUserLambdaQueryWrapper.eq(SysUser::getId,feedback.getUserId())
                                .select(SysUser::getAccount);
                SysUser sysUser = sysUserMapper.selectOne(sysUserLambdaQueryWrapper);
                feedBackVo.setAccount(sysUser.getAccount());
                feedBackVo.setCreateDate(new DateTime(feedback.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
                return feedBackVo;
            }
        }).collect(Collectors.toList());
        PageResult<FeedBackVo> feedBackVoPageResult = new PageResult<>();
        feedBackVoPageResult.setList(feedBackVoList);
        feedBackVoPageResult.setTotal(total);
        return Result.success(feedBackVoPageResult);
    }
}
