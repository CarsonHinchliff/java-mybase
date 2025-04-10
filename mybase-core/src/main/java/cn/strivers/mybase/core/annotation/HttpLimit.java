package cn.strivers.mybase.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 访问限制
 * 默认0.5秒
 *
 * @author mozhu
 * @date 2023-03-17 14:20:33
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpLimit {
    long interval() default 500L;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}