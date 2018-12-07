package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 遵循服务的注解，如果遵循此服务则会被YPC发现并且解析出来
 *
 *  * @version v1.0
 * @author angyang
 * @date 2018/11/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface YpcService {
}
