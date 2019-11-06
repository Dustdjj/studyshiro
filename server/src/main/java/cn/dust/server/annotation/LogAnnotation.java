package cn.dust.server.annotation;

import java.lang.annotation.*;

/**
 * @Author: dust
 * @Date: 2019/10/21 13:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String value() default "";
}
