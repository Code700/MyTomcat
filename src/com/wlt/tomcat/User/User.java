package com.wlt.tomcat.User;

import com.wlt.tomcat.inter.HttpServlet;
import com.wlt.tomcat.modle.Content_Type;
import com.wlt.tomcat.modle.Responce;
import com.wlt.tomcat.server.ServerHolder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author 王立腾
 * @date 2019-09-15 12:04
 */
public class User implements HttpServlet {

    @Override
    public void servlet(Responce responce, ServerHolder.Writer writer) {
        try {
            String requestURI = responce.getRequest().getRequestURI();
            System.out.println("requestURI:" + requestURI);

            responce.setContentType(Content_Type.TXT);
            String s;
            if (requestURI.startsWith("/server/User?name=")) {
                String[] name = requestURI.split("=");
                String s1 = URLDecoder.decode(name[1]);

                if (s1.equals("王立腾") || s1.equals("wangliteng")) {
                    s = "大胆铲屎官,还不快快交出猫粮，喵喵喵……";
                } else {
                    s = "欢迎" + s1 + "来看本喵，本喵很高兴，喵喵喵……";
                }
            } else {
                s = "大胆二哈，竟敢闯入本喵的领地，喵喵喵……";
            }

            writer.writer(responce, s.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
