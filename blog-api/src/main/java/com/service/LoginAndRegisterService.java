package com.service;

import com.pojo.SysUser;
import com.vo.Result;
import com.vo.params.LoginParams;

public interface LoginAndRegisterService {

    /*
    * 登录功能,之后要获取用户信息返回给前端
    * */
    Result login(LoginParams loginParam);

    /*
    * 验证token是否合法
    * */
    SysUser checkToken(String token);

    /*
    * 退出登录
    * */
    Result logout(String token);

    /*
    * 注册
    * */
    Result register(LoginParams loginParams);

    /*
    * 发送验证码
    * */
    void sendMsg(String email, String subject, String context);
}
