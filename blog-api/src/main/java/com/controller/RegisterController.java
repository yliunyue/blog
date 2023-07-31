package com.controller;

import com.service.LoginAndRegisterService;
import com.vo.Result;
import com.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private LoginAndRegisterService loginAndRegisterService;
    @PostMapping
    public Result register(@RequestBody LoginParams loginParams){
        //sso 单点登录
        return loginAndRegisterService.register(loginParams);
    }
}
