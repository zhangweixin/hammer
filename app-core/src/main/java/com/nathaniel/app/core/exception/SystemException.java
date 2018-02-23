package com.nathaniel.app.core.exception;

public class SystemException extends BaseException {

    public SystemException(String code, String msg, Throwable cause) {
        super(code, msg, cause);
    }

    public SystemException(String code, String msg) {
        super(code, msg);
    }

}
