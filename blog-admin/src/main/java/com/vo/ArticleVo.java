package com.vo;

import com.entity.Category;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;
@Data
public class ArticleVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Integer commentCounts;
    private String title;
    private String summary;
    private Integer viewCounts;
    private String author;//作者
    private String createDate;//创建时间
    private List<TagVo> tags;
    private Category category;
}
