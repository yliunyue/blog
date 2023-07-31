package com.vo.params;

import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {
    private Long id;
    private ArticleBodyParam body;
    private CategoryVo category;
    private String summary;//摘要
    private List<TagVo> tags;
    private String title;
}
