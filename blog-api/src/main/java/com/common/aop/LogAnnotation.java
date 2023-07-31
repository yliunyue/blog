package com.common.aop;

import java.lang.annotation.*;
//type代表可以放在类里面，METHOD标识可以放在方法上
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default  "";
    String operation() default "";
}
