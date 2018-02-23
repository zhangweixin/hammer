package com.nathaniel.app.mvc.util;

import com.nathaniel.app.mvc.model.AppResponse;

public class AppResponseBuilder {

    private static final String OK_CODE           = "000000";
    private static final String UNKNOW_ERROR_CODE = "999999";

    public static <T> AppResponse<T> successResponse(T data) {
        AppResponse<T> appResponse = new AppResponse<T>();
        appResponse.setCode(OK_CODE);
        appResponse.setData(data);
        return appResponse;
    }

    public static <T> AppResponse<T> unknowErrorResponse() {
        AppResponse<T> appResponse = new AppResponse<T>();
        appResponse.setCode(UNKNOW_ERROR_CODE);
        return appResponse;
    }

    public static <T> AppResponse<T> errorResponse(String code, String message) {
        AppResponse<T> appResponse = new AppResponse<T>();
        appResponse.setCode(code);
        appResponse.setMessage(message);
        return appResponse;
    }

}
