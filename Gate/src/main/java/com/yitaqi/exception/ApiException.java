package com.yitaqi.exception;

/**
 * api 接口 参数错误异常
 * @author xue
 */
public class ApiException extends RuntimeException {


    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
        super();
    }
}
