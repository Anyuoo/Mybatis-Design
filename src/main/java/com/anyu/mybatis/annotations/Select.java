package com.anyu.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * sql查询语句注解
 */
//生命周期
@Retention(RetentionPolicy.RUNTIME)
//出现位置，出现在方法上
@Target(ElementType.METHOD)

public @interface Select {
    /**
     * 配置sql语句
     *
     * @return
     */
    String value();
}
