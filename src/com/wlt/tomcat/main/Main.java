package com.wlt.tomcat.main;

import com.wlt.tomcat.server.Server;

/**
 * @author 王立腾
 * @date 2019-09-14 18:41
 */
public class Main {

    public static void main(String[] args) {
        //开启服务器
        Server.open().server(80);
    }

}
