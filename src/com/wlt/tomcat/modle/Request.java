package com.wlt.tomcat.modle;

import java.nio.channels.SelectionKey;
import java.util.List;

/**
 * @author 王立腾
 * @date 2019-09-14 19:00
 */
public class Request {
    private String method; // 请求方式
    private String requestURI; // 请求的uri
    private String version; //http协议版本
    private List<Header> headers; // 封装请求头
    private final SelectionKey selectionKey; // 用于获取通道


    /*****************************************************************************************/
    /*****************************************************************************************/

    public Request(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public Request setRequestURI(String requestURI) {
        this.requestURI = requestURI;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Request setVersion(String version) {
        this.version = version;
        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public Request setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }


    /*****************************************************************************************/
    /*****************************************************************************************/
}
