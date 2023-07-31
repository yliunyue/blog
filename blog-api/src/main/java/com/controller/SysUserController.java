package com.controller;

import com.dos.SysUserMessage;
import com.service.SysUserService;
import com.vo.Result;
import com.vo.params.UpdataEmailParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-06
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    /*
    * 查询个人信息
    * */
    @GetMapping("/userMessage")
    public Result findSysUserMessage(){
        return sysUserService.findSysUserMessage();
    }

    /*
    * 判断修改的邮箱的验证码
    * */
    @PostMapping("/isCode")
    public Result isCode(String code){
        return sysUserService.isCode(code);
    }

    /*
    * 修改邮箱
    * */
    @PostMapping("/updataEmail")
    public Result updataEmail(String code,String email){

        return sysUserService.updataEmail(code,email);
    }

    @PostMapping("/updataMes")
    public Result updataMes(String avatar,String nickname,@RequestHeader("Authorization") String token){
        System.out.println("修改信息接口token："+token);
        return sysUserService.updataMes(avatar,nickname,token);
    }

}
