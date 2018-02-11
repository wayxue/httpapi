package com.yitaqi.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 告诉我们的API网关这个方法需要往外暴露出去
 * @author xue
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface APIMapping {

    String value();
    // 登录检测
    boolean checkLogin() default false;
}
