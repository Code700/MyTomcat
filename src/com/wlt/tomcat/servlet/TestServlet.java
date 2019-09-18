package com.wlt.tomcat.servlet;

import com.wlt.tomcat.anno.WebServlet;
import com.wlt.tomcat.inter.HttpServlet;
import com.wlt.tomcat.modle.Content_Type;
import com.wlt.tomcat.modle.Responce;
import com.wlt.tomcat.server.ServerHolder;
import com.wlt.tomcat.utils.FileUtils;

/**
 * @author 王立腾
 * @date 2019-09-18 8:58
 */
@WebServlet(urlPatterns = "/server/TestServlet")
public class TestServlet implements HttpServlet {
    @Override
    public void servlet(Responce responce, ServerHolder.Writer writer) {
        byte[] content = FileUtils.getContent("webapp/images/a.jpg");
        responce.setContentType(Content_Type.JPG);
        writer.writer(responce, content);
    }
}
