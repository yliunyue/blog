package com.vo.params;

import lombok.Data;

import java.util.List;

@Data
public class QuestionListVo {
    private List<QuestionVo> questionVoList;
    private Long total;
}
