package com.wlt.tomcat.exception;

/**
 * @author 王立腾
 * @date 2019-09-15 18:29
 */
public class NotContentTypeRuntimeException extends RuntimeException {
    public NotContentTypeRuntimeException() {
    }

    public NotContentTypeRuntimeException(String message) {
        super(message);
    }
}
