package com.wlt.tomcat.parse_config;

import com.wlt.tomcat.exception.ClassNotLikeRuntimeException;
import com.wlt.tomcat.inter.HttpServlet;
import com.wlt.tomcat.inter.ParseHttpInfo;
import com.wlt.tomcat.inter.ServerletConcurrentHashMap;
import com.wlt.tomcat.utils.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 王立腾
 * @date 2019-09-15 16:41
 */
public class ParseServerHttpInfo implements ParseHttpInfo {

    /**
     * 读取并解析资源下的配置文件
     *
     * @param filePath
     */
    @Override
    public void parse(String filePath) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(filePath));
            String property = properties.getProperty("server-info");

            System.out.println(property);

            String[] strings = property.split(";");

            for (String string : strings) {
                if (StringUtils.isValid(string)) {
                    String[] split = string.split(",");

                    String uri = split[0];
                    String classAbsPaht = split[1];

                    Class aClass = Class.forName(classAbsPaht);

                    Class[] interfaces = aClass.getInterfaces();
                    boolean temp = false;
                    for (Class interf : interfaces) {
                        if (interf == HttpServlet.class) {
                            temp = true;
                            break;
                        }
                    }

                    if (temp) {
                        HttpServlet servlat = (HttpServlet) aClass.newInstance();
                        ServerletConcurrentHashMap.chm.put(uri, servlat);

                    } else {
                        try {
                            throw new ClassNotLikeRuntimeException("类型不匹配，请检查处理类是否实现了HttpServlet接口");
                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }
}
