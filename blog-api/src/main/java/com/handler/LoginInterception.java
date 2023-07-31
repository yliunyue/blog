package com.handler;

import com.alibaba.fastjson.JSON;
import com.pojo.SysUser;
import com.service.LoginAndRegisterService;
import com.utils.UserThreadLocal;
import com.vo.ErrorCode;
import com.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterception implements HandlerInterceptor {
    @Autowired
    private LoginAndRegisterService loginAndRegisterService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行方法之前执行。
        //1.判断请求的接口路径是不是handlermethod（contloller）
        if (!(handler instanceof HandlerMethod)) {
            //handler可能为资源handler（requestResourceHandler）
            return true;
        }
        //2.判断token是否为空，为空，未登录
        String token = request.getHeader("Authorization");
        if (token==null){
            token=request.getHeader("Oauth-Token");
        }
        log.info("===========request start=========");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}", requestURI);
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("===========request end============");

        //3.如果token为空，登录验证，验证token
        if (StringUtils.isBlank(token)) {
            //未登录
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //4.认证成功，放行
        SysUser sysUser = loginAndRegisterService.checkToken(token);
        if (sysUser == null) {
            //未登录
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //登录验证成功，放行 
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //用完了threadlocal要删除，如果不删除ThreadLocal中用完的信息，会有内存泄露的风险
        UserThreadLocal.remove();
    }
}
