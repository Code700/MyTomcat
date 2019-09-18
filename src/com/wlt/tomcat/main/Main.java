package com.wlt.tomcat.main;

import com.wlt.tomcat.parse_config.AnnoParseServerHttpInfo;
import com.wlt.tomcat.server.Server;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author 王立腾
 * @date 2019-09-14 18:41
 */
public class Main {

    public static void main(String[] args) {
        //开启服务器
        Server.open().server(80);

        //AnnoParseServerHttpInfo info = new AnnoParseServerHttpInfo();
        //info.parse("src/com/wlt/tomcat/servlet");


    }


    /***********************************************************************************************/
    /**
     * 一个有趣的功能
     * 获取一个类的泛型
     */
    public static void fascinating() {
        try {
            //拿ArrayList<String>为例
            //思路是可以把一个这个类带参数作为一个方法的参数，然后通过反射最后获取到该类的泛型
            Class<Main> aClass = Main.class;
            Method method = aClass.getMethod("coordinate", ArrayList.class);
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            ParameterizedType type = (ParameterizedType) genericParameterTypes[0];
            Type argument = type.getActualTypeArguments()[0];
            System.out.println(argument);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    public static void coordinate(ArrayList<String> al) {
    }

}
