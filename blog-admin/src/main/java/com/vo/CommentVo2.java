package com.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CommentVo2 {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long articleId;//多
    private String title;
    private UserVo author;
    private String content;
    // private List<CommentVo> childrens;
    private String createDate;
    private Integer level;
    private UserVo toUser;
}
