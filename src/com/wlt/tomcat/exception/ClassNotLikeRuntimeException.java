package com.wlt.tomcat.exception;

/**
 * @author 王立腾
 * @date 2019-09-15 17:11
 */
public class ClassNotLikeRuntimeException extends RuntimeException {

    public ClassNotLikeRuntimeException() {
    }

    public ClassNotLikeRuntimeException(String message) {
        super(message);
    }
}
