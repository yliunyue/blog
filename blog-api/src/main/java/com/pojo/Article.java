package com.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Article {
    public static final int Article_TOP = 1;
    public static final int Article_Common = 0;
    private Long id;
    private Integer commentCounts;
    private String title;
    private String summary;
    private Integer viewCounts;

    private Long authorId;//作者id
    private Long bodyId;//文章内容id
    private Long categoryId;//类别id
    private Integer weight;//置顶
    private Long createDate;//创建时间
}
