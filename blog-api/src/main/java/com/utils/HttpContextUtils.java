package com.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpContextUtils {
    //获取HttpServletRequest请求
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
