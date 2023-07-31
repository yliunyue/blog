package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.entity.Adminfeed;
import com.entity.Feedback;
import com.mapper.AdminfeedMapper;
import com.mapper.FeedbackMapper;
import com.mapper.SysUserMapper;
import com.pojo.SysUser;
import com.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.UserThreadLocal;
import com.vo.Result;
import com.vo.params.AnswerParam;
import com.vo.params.FeedbackVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-03-31
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private AdminfeedMapper adminfeedMapper;

    @Override
    public Result feedbackList() {
        LambdaQueryWrapper<Feedback> queryWrapper=new LambdaQueryWrapper<>();
        List<Feedback> feedbacks = feedbackMapper.selectList(queryWrapper);
        List<FeedbackVo> feedbackVoList = feedbacks.stream().map(new Function<Feedback, FeedbackVo>() {
            @Override
            public FeedbackVo apply(Feedback feedback) {
                FeedbackVo feedbackVo = new FeedbackVo();
                BeanUtils.copyProperties(feedback, feedbackVo);
                SysUser sysUser = sysUserMapper.selectById(feedback.getUserId());
                feedbackVo.setUserName(sysUser.getNickname());
                //查询反馈信息
                LambdaQueryWrapper<Adminfeed> queryWrapper1=new LambdaQueryWrapper<>();
                queryWrapper1.eq(Adminfeed::getFeedbackId,feedback.getId()).last("limit 1");
                Adminfeed adminfeed = adminfeedMapper.selectOne(queryWrapper1);
                feedbackVo.setAdminfeed(adminfeed);
                return feedbackVo;
            }
        }).collect(Collectors.toList());
        return Result.success(feedbackVoList);
    }

    @Override
    public Result feedback(AnswerParam answerParam) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        Feedback feedback = new Feedback();
        feedback.setCreateDate(System.currentTimeMillis());
        feedback.setContent(answerParam.getContent());
        feedback.setUserId(userId);
        int insert = feedbackMapper.insert(feedback);
        return Result.success("反馈成功");
    }
}
