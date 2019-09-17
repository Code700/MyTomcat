package com.wlt.tomcat.parse_config;

import com.wlt.tomcat.exception.ClassNotLikeRuntimeException;
import com.wlt.tomcat.inter.HttpServlet;
import com.wlt.tomcat.inter.ParseHttpInfo;
import com.wlt.tomcat.inter.ServerletConcurrentHashMap;
import com.wlt.tomcat.modle.Config_path;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * @author 王立腾
 * @date 2019-09-17 18:01
 */
public class XmlDomParse implements ParseHttpInfo {

    @Override
    public void parse(String filePath) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new FileInputStream(filePath));

            Element rootElement = document.getRootElement();

            List<Element> servlets = rootElement.elements("servlet");

            HashMap<String, String> hashMap = new HashMap<>();
            for (Element servlet : servlets) {
                String name = servlet.element("servlet-name").getText();
                String clazz = servlet.element("servlet-class").getText();
                hashMap.put(name, clazz);

            }

            List<Element> mappings = rootElement.elements("servlet-mapping");

            for (Element maping : mappings) {
                String mName = maping.element("servlet-name").getText();
                String uri = maping.element("url-pattern").getText();

                String clazz = hashMap.get(mName);

                Class aClass = Class.forName(clazz);
                Class[] interfaces = aClass.getInterfaces();

                boolean temp = false;
                for (Class cla : interfaces) {
                    if (cla == HttpServlet.class) {
                        temp = true;
                        break;
                    }
                }
                if (temp) {
                    HttpServlet instance = (HttpServlet) aClass.newInstance();
                    ServerletConcurrentHashMap.chm.put(uri, instance);
                } else {
                    try {
                        throw new ClassNotLikeRuntimeException("类型不匹配，请检查处理类是否实现了HttpServlet接口");
                    } catch (ClassCastException cce) {
                        cce.printStackTrace();
                    }
                }


            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
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
