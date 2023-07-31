package com.dos;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserMessage implements Serializable {

    private Long id;
    /**
     * 账号
     */
    private String account;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 注册时间
     */
    private String createDate;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    private Integer fans;

    private Integer FollowerNums;
}
