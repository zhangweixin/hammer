package com.nathaniel.app.core.exception;

import java.text.MessageFormat;

public class BaseException extends RuntimeException {
    private String    code;
    private String    msg;
    private Throwable cause;

    public BaseException(String code, String msg, Throwable cause) {
        this(merge(code, msg), cause);
    }

    public BaseException(String code, String msg) {
        this(merge(code, msg), (Throwable) null);
    }

    private BaseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    private static String merge(String code, String msg) {
        return MessageFormat.format("[errorCode:{0},errorMsg:{1}]", code, msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
