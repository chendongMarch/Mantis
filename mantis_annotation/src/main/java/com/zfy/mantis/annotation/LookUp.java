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
public @interface LookUp {

    // Mark param's name or service name.
    String value();

    // If required, app will be crash when value is null.
    // Primitive type wont be check!
    boolean required() default false;

    // Description of the field
    String desc() default "";
}
