package com.wlt.tomcat.server;

import com.wlt.tomcat.exception.NotContentTypeRuntimeException;
import com.wlt.tomcat.inter.HttpServlet;
import com.wlt.tomcat.inter.ServerletConcurrentHashMap;
import com.wlt.tomcat.modle.*;
import com.wlt.tomcat.utils.FileUtils;
import com.wlt.tomcat.utils.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;

/**
 * @author 王立腾
 * @date 2019-09-14 18:49
 */
public class ServerHolder {

    /**
     * 处理接收事件
     *
     * @param selectionKey 任务对象
     * @return
     */
    public SocketChannel connectSocketChannel(SelectionKey selectionKey) {
        try {
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();

            SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking(false);

            Selector selector = selectionKey.selector();
            socketChannel.register(selector, SelectionKey.OP_READ);

            return socketChannel;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析请求
     *
     * @param selectionKey
     * @return
     */
    public Request parse(SelectionKey selectionKey) {
        //初始化Request对象
        Request request = new Request(selectionKey);
        try {
            //用来拼接存储读取到的数据
            StringBuilder stringBuilder = new StringBuilder();
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer allocate = ByteBuffer.allocate(1024);

            int read = channel.read(allocate);
            while (read > 0) {
                allocate.flip();
                stringBuilder.append(new String(allocate.array(), 0, read));
                allocate.clear();
                read = channel.read(allocate);
            }
            String string = stringBuilder.toString();

            //如果没有读取到数据，就返回一个null
            if (string.equals("") || null == string) {
                return null;
            }


            //对得到的数据进行解析
            String[] strings = string.split("\r\n");

            //请求行
            String httpRequestLine = strings[0];
            String[] data = httpRequestLine.split(" ");
            //System.out.println("data:" + httpRequestLine);
            request.setMethod(data[0]);
            request.setRequestURI(data[1]);
            request.setVersion(data[2]);

            //请求头
            ArrayList<Header> list = new ArrayList<>();
            for (int i = 1; i < strings.length; i++) {
                String headerInfo = strings[i];
                String[] item = headerInfo.split(": ");
                Header header = new Header(item[0], item[1]);
                list.add(header);
            }
            request.setHeaders(list);

            return request;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 响应并反馈
     *
     * @param selectionKey
     * @param request
     */
    public void couple(SelectionKey selectionKey, Request request) {
        try {
            //初始化Responce对象
            Responce responce = new Responce(selectionKey, request);

            //获取请求过来的URI
            String requestURI = request.getRequestURI();
            File file = new File(requestURI.substring(1));

            boolean isfile = file.exists();

            ByteBuffer buffer = null;

            //文件是否存在,或者是头标文件请求
            if (isfile || requestURI.equals("/favicon.ico")) {
                //判断URI是否访问的是一个文件夹
                if (file.isDirectory()) {
                    responce.setStatus("202");
                    responce.setDes("Disagreeing to Requests");
                    responce.setContentType(Content_Type.TXT);
                    //文件夹
                    String content = "拒绝访问文件夹，喵喵喵…";
                    buffer = ByteBuffer.wrap(content.getBytes("UTF-8"));

                } else {
                    responce.setStatus("200");
                    responce.setDes("OK");

                    //是一个文件，通过后缀名得到该文件的所属类型，只是部分常用文件类型

                    if (requestURI.endsWith("txt")) {
                        responce.setContentType(Content_Type.TXT);
                    } else if (requestURI.endsWith("jpg")) {
                        responce.setContentType(Content_Type.JPG);
                    } else if (requestURI.endsWith("png")) {
                        responce.setContentType(Content_Type.PNG);
                    } else if (requestURI.endsWith("gif")) {
                        responce.setContentType(Content_Type.GIF);
                    } else if (requestURI.endsWith("ico")) {
                        responce.setContentType(Content_Type.ICO);
                    }

                    byte[] content;
                    //文件
                    if (requestURI.equals("/favicon.ico")) {
                        content = FileUtils.getContent(Config_path.SERVER_ICO);
                    } else {
                        content = FileUtils.getContent(requestURI.substring(1));
                    }

                    if (content != null) {
                        buffer = ByteBuffer.wrap(content);
                    } else {
                        //需要遍历修改相应头信息，并需改文件类型
                    }

                }

            } else {
                //文件不存在，或是直接请求端口号
                String content;
                if (requestURI.equals("/")) {
                    //如果只是一个"/"说明访问的是服务器的默认端口号
                    responce.setStatus("200");
                    responce.setDes("MAIN SERVER!");

                    content = "欢迎访问【大菊为重】服务器，喵喵喵…";
                    buffer = ByteBuffer.wrap(content.getBytes("UTF-8"));

                } else {
                    //文件没有找到
                    responce.setStatus("404");
                    responce.setDes("Not File!");

                    content = "没有找到你要的文件，喵喵喵喵…";
                    buffer = ByteBuffer.wrap(content.getBytes("UTF-8"));
                }
                responce.setContentType(Content_Type.TXT);
            }

            write(responce, buffer.array());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理动态事件
     *
     * @param selectionKey
     * @param request
     */
    public void server(SelectionKey selectionKey, Request request) {

        Responce responce1 = new Responce(selectionKey, request);
        System.out.println(request.getRequestURI());
        String uri = request.getRequestURI();
        if (uri.indexOf("?") != -1) {
            uri = uri.split("[?]")[0];
        }
        System.out.println("uri:" + uri);

        if (ServerletConcurrentHashMap.chm.containsKey(uri)) {

            //通过读取配置文件信息，反射出相应的请求处理对象
            //调用servlet处理方法，并根据处理，回调回来做出相应的反馈
            HttpServlet httpServlet = ServerletConcurrentHashMap.chm.get(uri);
            httpServlet.servlet(responce1, (responce, bytes) -> write(responce, bytes));

        } else {
            try {
                String des = "404 没有这样的处理事件，喵喵喵……!";
                responce1.setContentType(Content_Type.TXT).setStatus("404").setDes(des);
                write(responce1, des.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 回调结果处理接口
     */
    public interface Writer {
        void writer(Responce responce, byte[] bytes);
    }

    /**
     * 给客户端进行响应，根据业务逻辑处理的结果，响应数据
     */
    public void write(Responce responce, byte[] bytes) {
        try {
            // 按照我们的Http响应协议的格式给客户端传输数据
            // 构建响应行

            if (StringUtils.isValid(responce.getContentType())) {
                if (StringUtils.isValid(responce.getStatus()))
                    responce.setStatus("200");
                if (StringUtils.isValid(responce.getDes()))
                    responce.setDes("OK");

                String responseLine = responce.getVersion() + " " + responce.getStatus() + " " + responce.getDes() + "\r\n";

                // 构建响应头
                StringBuilder sb = new StringBuilder();
                for (Header header : responce.getHeaders()) {
                    sb.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
                }
                String responseHeader = sb.toString();

                System.out.println(responseHeader);

                // 响应空行
                String emptyLine = "\r\n";

                // 把响应行， 响应头，响应空行拼接成一个字符串
                String result = responseLine + responseHeader + emptyLine;


                // 写数据
                SocketChannel socketChannel = (SocketChannel) responce.getSelectionKey().channel();
                ByteBuffer byteBuffer = ByteBuffer.wrap(result.getBytes("UTF-8"));
                socketChannel.write(byteBuffer);

                // 写响应体数据
                ByteBuffer body = ByteBuffer.wrap(bytes);
                socketChannel.write(body);
                socketChannel.close();

            } else {
                String des = "405 请求异常，喵喵喵……!";
                responce.setContentType("text/html;charset=UTF-8").setStatus("405").setDes(des);
                write(responce, des.getBytes("UTF-8"));

                try {
                    throw new NotContentTypeRuntimeException(responce.getRequest().getRequestURI() + "请求没有设置ContentType属性！");
                } catch (NotContentTypeRuntimeException nctre) {
                    nctre.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
