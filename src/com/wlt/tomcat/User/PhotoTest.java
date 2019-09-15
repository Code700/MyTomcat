package com.wlt.tomcat.User;

import com.wlt.tomcat.inter.HttpServlet;
import com.wlt.tomcat.modle.Responce;
import com.wlt.tomcat.server.ServerHolder;
import com.wlt.tomcat.utils.FileUtils;

/**
 * @author 王立腾
 * @date 2019-09-15 20:18
 */
public class PhotoTest implements HttpServlet {
    @Override
    public void servlet(Responce responce, ServerHolder.Writer writer) {
        byte[] content = FileUtils.getContent("webapp/a.jpg");
        responce.setContentType("image/jpeg");
        writer.writer(responce, content);
    }
}
