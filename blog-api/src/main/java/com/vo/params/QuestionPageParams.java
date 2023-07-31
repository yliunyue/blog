package com.vo.params;

import lombok.Data;

@Data
public class QuestionPageParams {
    private int page = 1;

    private int pageSize = 10;
}
