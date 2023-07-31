package com.vo;

import lombok.Data;

@Data
public class SysUserVo {
    private String id;

    private String account;

    private String avatar;

    private String createDate;

    private Boolean deleted;

    private String email;

    private String lastLogin;

    private String nickname;

    private String password;

    private String salt;
}
