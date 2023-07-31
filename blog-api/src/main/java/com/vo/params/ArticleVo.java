package com.vo.params;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pojo.ArticleBody;
import com.pojo.Category;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class ArticleVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Integer commentCounts;
    private String title;
    private String summary;
    private Integer viewCounts;
    // private String author;//作者
    private UserVo author;//作者
    private String createDate;//创建时间
    private List<TagVo> tags;
    private ArticleBodyVo body;
    private CategoryVo category;
}
