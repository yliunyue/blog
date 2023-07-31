package com.controller;

import com.service.LoginAndRegisterService;
import com.utils.ValidateCodeUtils;
import com.vo.Result;
import com.vo.params.EmailMsgVo;
import com.vo.params.LoginParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {


    @Autowired
    private LoginAndRegisterService loginService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /*
    * 登录功能
    * */
    @PostMapping
    public Result login(@RequestBody LoginParams loginParam) {
        //登录 验证用户（访问用户表
        return loginService.login(loginParam);
    }

    @PostMapping("/sendMsg")
    public Result sendMsg(@RequestParam("email") String email){
        //邮箱
        log.info("邮箱：{}",email);
        String subject = "博客注册登录验证码";
        if (StringUtils.isNotEmpty(email)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            String context = "欢迎计算机技术交流博客系统，注册登录验证码为: " + code + ",五分钟内有效，请妥善保管!，有问题请联系龙哥（lhl）";
            log.info("code={}", code);
            // 真正地发送邮箱验证码
            loginService.sendMsg(email, subject, context);
            // 验证码由保存到session 优化为 缓存到Redis中，并且设置验证码的有效时间为 5分钟
            stringRedisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
            EmailMsgVo emailMsgVo = new EmailMsgVo();
            emailMsgVo.setMsg("验证码发送成功，请及时查看!");
            emailMsgVo.setCode(code);
            return Result.success(emailMsgVo);
        }
        return Result.fail(80000,"验证码发送失败，请重新输入!");
    }
}
