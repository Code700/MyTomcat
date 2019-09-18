package com.wlt.tomcat.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 王立腾
 * @date 2019-09-18 9:00
 * servlet配置信息的注解
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)

public @interface WebServlet {
    public String urlPatterns();
}
