package com.wlt.tomcat.server;

import com.wlt.tomcat.modle.Config_path;
import com.wlt.tomcat.modle.Request;
import com.wlt.tomcat.parse_config.ParseServerHttpInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 王立腾
 * @date 2019-09-14 18:41
 */
public class Server {

    private Server() {
        new Thread(() -> new ParseServerHttpInfo().parse(Config_path.SERVER_INFO)).start();
    }

    /**
     * 开启fu
     *
     * @return
     */
    public static Server open() {
        return Instance.server;
    }

    static class Instance {
        public static Server server = new Server();
    }

    /**
     * 服务器启动
     *
     * @param port 服务器端口号
     */
    public void server(int port) {
        try {

            //打开ServerSocketChannel通道，并绑定一个服务器，设置为非堵塞
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            //创建选择器对象，并把服务器通道注册到选择器,和设置了“接收”这个感兴趣的事件
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {

                //该方法是一个堵塞方法，返回已经准备就绪的通道数
                int select = selector.select();

                if (select != 0) {
                    //对任务对象进行遍历，分别处理每个任务
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();

                    while (iterator.hasNext()) {
                        //每个任务
                        SelectionKey selectionKey = iterator.next();
                        //持有任务去处理的帮助类
                        ServerHolder serverHolder = new ServerHolder();

                        if (selectionKey.isAcceptable()) {
                            //对连接任务处理
                            SocketChannel socketChannel = serverHolder.connectSocketChannel(selectionKey);

                        } else if (selectionKey.isReadable()) {

                            //获得到请求的request信息对象
                            Request request = serverHolder.parse(selectionKey);

                            //判断请求对象是否传过了是null,是null就不操作，
                            // 并从任务中删除掉这个请求，结束掉这次请求
                            if (null == request) {
                                selectionKey.cancel();
                                continue;
                            }

                            //我们定义凡是/server 开头的请求，这就是一个动态请求
                            if (request.getRequestURI().startsWith("/server")) {
                                serverHolder.server(selectionKey, request);
                            } else {
                                //根据request请求，给出相应的响应
                                serverHolder.couple(selectionKey, request);
                            }

                        }
                        //执行完，从任务集合中删除这个任务对象
                        iterator.remove();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
