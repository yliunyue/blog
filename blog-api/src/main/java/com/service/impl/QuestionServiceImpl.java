package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapper.SysUserMapper;
import com.pojo.Answer;
import com.pojo.Question;
import com.mapper.QuestionMapper;
import com.pojo.SysUser;
import com.service.AnswerService;
import com.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.UserThreadLocal;
import com.vo.ErrorCode;
import com.vo.Result;
import com.vo.params.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-03-31
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Result pageQuestion(QuestionPageParams pageParams) {
        //查询出所有的问题列表，按照时间和状态排序。先状态再时间
        IPage<Question> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Question> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Question::getStatus).orderByDesc(Question::getCreateDate);
        IPage<Question> page1 = questionMapper.selectPage(page, lambdaQueryWrapper);
        List<Question> records = page1.getRecords();
        List<QuestionVo> questionVoList = records.stream().map(new Function<Question, QuestionVo>() {
            @Override
            public QuestionVo apply(Question question) {
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
            }
        }).collect(Collectors.toList());
        long total = page1.getTotal();
        QuestionListVo questionListVo = new QuestionListVo();
        questionListVo.setQuestionVoList(questionVoList);
        questionListVo.setTotal(total);
        return Result.success(questionListVo);
    }

    @Override
    public Result findQuestion(PageSearchParam pageParams) {
        //查询出所有的问题列表，按照时间和状态排序。先状态再时间
        IPage<Question> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
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
        QuestionListVo questionListVo = new QuestionListVo();
        questionListVo.setQuestionVoList(questionVoList);
        questionListVo.setTotal(total);
        return Result.success(questionListVo);
    }

    @Override
    @Transactional
    public Result askQuestion(AskQuestionParam askQuestionParam) {
        //获取当前用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        Question question = new Question();
        question.setQuestion(askQuestionParam.getQuestion());
        question.setCreateDate(System.currentTimeMillis());
        question.setUserId(userId);
        question.setStatus(0);
        int insert = questionMapper.insert(question);
        if (insert <= 0) {
            return Result.fail(11111, "问题发布失败，稍后重试");
        }
        return Result.success("您的问题已发布");
    }

    @Override
    public Result answerQuestion(AnswerParam answerParam, Integer questionId) {
        //获取当前用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        Answer answer = new Answer();
        answer.setQuestionId(questionId);
        answer.setUserId(userId);
        answer.setCreateDate(System.currentTimeMillis());
        answer.setContent(answerParam.getContent());
        boolean save = answerService.save(answer);
        return Result.success("已发布");
    }

    @Override
    @Transactional
    public Result uploadStatus(Integer questionId) {
        //先获取当前登录用户信息
        //获取当前用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //根据id查询问题
        Question question = questionMapper.selectById(questionId);
        if (!userId.equals(question.getUserId())) {
            //只有问题发布者才能确认解决问题
            return Result.fail(ErrorCode.NOT_YOU_QUESTION.getCode(), ErrorCode.NOT_YOU_QUESTION.getMsg());
        }
        LambdaUpdateWrapper<Question> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Question::getId, questionId)
                .set(Question::getStatus, 1);
        questionMapper.update(null, updateWrapper);
        return Result.success("问题已解决");
    }
}
