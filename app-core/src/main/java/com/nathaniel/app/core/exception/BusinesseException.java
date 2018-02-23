package com.nathaniel.app.core.exception;

public class BusinesseException extends BaseException {

    public BusinesseException(String code, String msg, Throwable cause) {
        super(code, msg, cause);
    }

    public BusinesseException(String code, String msg) {
        super(code, msg);
    }
}
