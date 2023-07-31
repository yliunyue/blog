package com.controller;

import com.service.LoginAndRegisterService;
import com.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logout")
public class LogOutController {

    @Autowired
    private LoginAndRegisterService loginService;
    /*
    * 退出登录
    * */
    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token) {
        //登录 验证用户（访问用户表
        return loginService.logout(token);
    }
}
