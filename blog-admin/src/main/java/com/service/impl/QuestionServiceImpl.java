package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Answer;
import com.entity.Question;
import com.entity.SysUser;
import com.mapper.QuestionMapper;
import com.mapper.SysUserMapper;
import com.service.AnswerService;
import com.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.AnswerVo;
import com.vo.params.PageParam;
import com.vo.params.PageResult;
import com.vo.QuestionVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private AnswerService answerService;

    @Override
    public Result findQuestion(PageParam pageParams) {
        //查询出所有的问题列表，按照时间和状态排序。先状态再时间
        IPage<Question> page = new Page<>(pageParams.getCurrentPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Question> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(pageParams.getQueryString()),Question::getQuestion,pageParams.getQueryString())
                .orderByAsc(Question::getStatus).orderByDesc(Question::getCreateDate);

        IPage<Question> page1 = questionMapper.selectPage(page, lambdaQueryWrapper);
        List<Question> records = page1.getRecords();

        List<QuestionVo> questionVoList = records.stream().map(question -> {
            QuestionVo questionVo = new QuestionVo();
            BeanUtils.copyProperties(question, questionVo);
            //用户名
            SysUser sysUser = sysUserMapper.selectById(question.getUserId());
            questionVo.setUserName(sysUser.getNickname());
            //时间
            questionVo.setCreateDate(new DateTime(question.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            //答案,根据问题id查找答案列表
            List<AnswerVo> answerVoList = answerService.findAnswerById(question.getId());
            questionVo.setAnswerList(answerVoList);
            return questionVo;
        }).collect(Collectors.toList());
        long total = page1.getTotal();

        PageResult<QuestionVo> questionVoPageResult = new PageResult<>();
        questionVoPageResult.setTotal(total);
        questionVoPageResult.setList(questionVoList);
        return Result.success(questionVoPageResult);
    }

    @Override
    @Transactional
    public Result deleteQuestion(Long id) {
        //删除问题，同时要删除答案
        LambdaQueryWrapper<Answer> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Answer::getQuestionId,id);
        boolean remove = answerService.remove(queryWrapper);
        //删除问题
        int i = questionMapper.deleteById(id);
        return Result.success("删除成功");
    }
}
