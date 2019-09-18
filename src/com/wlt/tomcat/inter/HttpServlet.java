package com.wlt.tomcat.inter;

import com.wlt.tomcat.anno.WebServlet;
import com.wlt.tomcat.modle.Responce;
import com.wlt.tomcat.server.ServerHolder;

/**
 * @author 王立腾
 * @date 2019-09-15 15:47
 */

public interface HttpServlet {

    //只有实现了该接口才能处理请求
    void servlet(Responce responce, ServerHolder.Writer writer);

}
