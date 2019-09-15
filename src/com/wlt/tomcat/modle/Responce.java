package com.wlt.tomcat.modle;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王立腾
 * @date 2019-09-14 19:44
 */
public class Responce {

    private final String version = "HTTP/1.1";
    private String status;
    private String des;

    private String contentType;
    private List<Header> headers = new ArrayList<>();

    private final SelectionKey selectionKey;
    private final Request Request;

    public Responce(SelectionKey selectionKey, com.wlt.tomcat.modle.Request request) {
        this.selectionKey = selectionKey;
        this.Request = request;
    }


    /************************************************************/
    /************************************************************/
    public String getVersion() {
        return version;
    }


    public String getStatus() {
        return status;
    }

    public Responce setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getDes() {
        return des;
    }

    public Responce setDes(String des) {
        this.des = des;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public Responce setContentType(String contentType) {
        this.contentType = contentType;
        Header header = new Header();
        header.setName("Content-Type");
        header.setValue(contentType);
        headers.add(header);

        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public Responce setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public com.wlt.tomcat.modle.Request getRequest() {
        return Request;
    }

    /************************************************************/
    /************************************************************/
}
