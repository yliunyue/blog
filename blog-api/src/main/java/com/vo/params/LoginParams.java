package com.vo.params;

import lombok.Data;

@Data
public class LoginParams {

    private String account;
    private String password;
    private String nickname;
    private String email;
    private String code;
}
