package com.zfy.mantis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CreateAt : 2019/1/29
 * Describe : 自动初始化
 *
 * @author chendong
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Lookup {

    // 默认分组
    int DEF_GROUP = -1;

    String AUTO     = "_";
    int    AUTO_NUM = -1;
    String SEP    = "_";
    String SUFFIX = "LOOKUP";

    // 注入的 key
    String value() default AUTO;

    // 注入的 int 类型的 key
    int numKey() default AUTO_NUM;

    // 附带参数
    int extra() default 0;

    // 本次注入的类型的分组，使用分组将注入分成多个模块
    int group() default DEF_GROUP;

    // 注入的 class
    Class clazz() default void.class;

    // 强制使用对象注入
    boolean obj() default false;

    // 如果是对象，则不允许为空
    boolean required() default false;

    // 自动生成注释
    String desc() default "";
}
