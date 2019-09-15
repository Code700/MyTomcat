package com.wlt.tomcat.inter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王立腾
 * @date 2019-09-15 15:46
 */
public interface ServerletConcurrentHashMap {

    //存放请求配置信息
    ConcurrentHashMap<String, HttpServlet> chm = new ConcurrentHashMap<String, HttpServlet>();

}
