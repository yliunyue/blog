package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mapper.SysUserMapper;
import com.pojo.Answer;
import com.mapper.AnswerMapper;
import com.pojo.SysUser;
import com.service.AnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.params.AnswerVo;
import com.vo.params.ArticleVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.util.Date;
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
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public List<AnswerVo> findAnswerById(Integer id) {
        LambdaQueryWrapper<Answer> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Answer::getQuestionId,id).orderByAsc(Answer::getCreateDate);
        List<Answer> answerList = answerMapper.selectList(queryWrapper);
        List<AnswerVo> answerVoList = answerList.stream().map(new Function<Answer, AnswerVo>() {
            @Override
            public AnswerVo apply(Answer answer) {
                AnswerVo answerVo = new AnswerVo();
                BeanUtils.copyProperties(answer, answerVo);
                answerVo.setCreateDate(new DateTime(answer.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
                //回答者昵称
                //用户名
                SysUser sysUser = sysUserMapper.selectById(answer.getUserId());
                answerVo.setUserName(sysUser.getNickname());
                return answerVo;
            }
        }).collect(Collectors.toList());
        return answerVoList;
    }
}
