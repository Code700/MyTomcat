package com.wlt.tomcat.parse_config;

import com.wlt.tomcat.anno.WebServlet;
import com.wlt.tomcat.exception.ClassNotLikeRuntimeException;
import com.wlt.tomcat.inter.HttpServlet;
import com.wlt.tomcat.inter.ParseHttpInfo;
import com.wlt.tomcat.inter.ServerletConcurrentHashMap;

import java.io.File;

/**
 * @author 王立腾
 * @date 2019-09-18 8:28
 */
public class AnnoParseServerHttpInfo implements ParseHttpInfo {
    public final String pack = "com.wlt.tomcat.servlet.";

    @Override
    public void parse(String filePath) {
        File file = new File(filePath);
        String[] list = file.list();

        for (String name : list) {
            try {
                String clazzPath = pack + name.replace(".java", "");

                System.out.println(clazzPath);

                Class aClass = Class.forName(clazzPath);

                Class[] interfaces = aClass.getInterfaces();
                boolean temp = false;
                for (Class inter : interfaces) {
                    if (inter == HttpServlet.class) {
                        temp = true;
                        break;
                    }
                }

                if (temp) {
                    if (aClass.isAnnotationPresent(WebServlet.class)) {
                        WebServlet annotation = (WebServlet) aClass.getAnnotation(WebServlet.class);
                        String patterns = annotation.urlPatterns();
                        HttpServlet instance = (HttpServlet) aClass.newInstance();
                        ServerletConcurrentHashMap.chm.put(patterns, instance);

                    } else {
                        try {
                            throw new ClassNotLikeRuntimeException(aClass.getName()+"没有引用WebServer注解");
                        } catch (ClassNotLikeRuntimeException cnr) {
                            cnr.printStackTrace();
                        }
                    }


                } else {
                    try {
                        throw new ClassNotLikeRuntimeException("没有实现HttpServlet");
                    } catch (ClassNotLikeRuntimeException cnr) {
                        cnr.printStackTrace();
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }


        }

    }
}
