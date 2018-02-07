package com.nathaniel.app.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import com.google.common.base.Splitter;

public abstract class BaseController {
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * @param request http请求对象
     * @param response http响应对象
     * @param handlerMethod 发生异常的controller请求处理方法
     * @param e 具体异常
     */
    @ExceptionHandler(Exception.class)
    public void handlerExcepion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception e) {
        if (isJsonRequest(handlerMethod)) {
            processJsonRequestException(request, response);
        } else if (isHtmlRequest(request)) {
            processHtmlRequestException(request, response);
        } else {
            processOtherMedisTypeRequestException(request, response);
        }

    }

    protected boolean isJsonRequest(HandlerMethod handlerMethod) {
        if (handlerMethod.getBeanType().isAnnotationPresent(RequestMapping.class)) {
            return true;
        } else if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
            return true;
        } else if (handlerMethod.getMethodAnnotation(RequestMapping.class) != null) {
            return true;
        }
        return false;
    }

    protected boolean isHtmlRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.ACCEPT);
        boolean isHtmlRequest = false;
        for (String accept : Splitter.on(",").splitToList(header)) {
            if (accept.equals(MediaType.TEXT_HTML_VALUE)) {
                isHtmlRequest = true;
            }
        }
        return isHtmlRequest;
    }

    protected void processJsonRequestException(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void processHtmlRequestException(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void processOtherMedisTypeRequestException(HttpServletRequest request, HttpServletResponse response) {
        processJsonRequestException(request, response);
    }
}
