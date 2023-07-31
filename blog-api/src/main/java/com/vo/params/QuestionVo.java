package com.vo.params;

import com.pojo.Answer;
import com.pojo.Question;
import lombok.Data;

import java.util.List;

@Data
public class QuestionVo  {
    private Integer id;

    private String question;

    private Long userId;

    private String userName;

    private String createDate;

    /**
     * 状态 0表示没解决 1表示简介了
     */
    private Integer status;
    private List<AnswerVo> answerList;
}
