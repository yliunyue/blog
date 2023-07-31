package com.handler;

import com.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 统一异常处理
 * */
//对加了controller注解的方法进行拦截处理
@ControllerAdvice
public class AllExceptionHandler {
    //进行异常处理，处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public Result doException(Exception exception) {
        exception.printStackTrace();
        //后续需要加日志
        return Result.fail(-999, "系统异常");
    }

}
