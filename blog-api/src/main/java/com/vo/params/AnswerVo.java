package com.vo.params;

import lombok.Data;

@Data
public class AnswerVo {
    private Integer id;

    private Long userId;

    private String userName;

    private Integer questionId;

    private String content;

    private String createDate;
}
