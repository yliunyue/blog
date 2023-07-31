package com.vo;

import com.entity.Adminfeed;
import com.entity.Feedback;
import lombok.Data;

@Data
public class FeedBackVo {
    private Integer id;

    private Long userId;

    private String content;

    private String createDate;

    private Adminfeed adminfeed;

    private String account;
}
