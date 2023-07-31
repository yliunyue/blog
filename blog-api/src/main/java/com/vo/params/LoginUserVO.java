package com.vo.params;

import lombok.Data;

/*
* 根据token获取用户信息对应的视图对象
* */
@Data
public class LoginUserVO {
    private Long id;
    private String account;
    private String nickname;
    private String avatar;
}
