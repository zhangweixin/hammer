/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nathaniel.app.mvc.controller;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nathaniel.app.core.exception.BaseException;
import com.nathaniel.app.core.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.nathaniel.app.core.exception.BusinesseException;
import com.nathaniel.app.mvc.model.AppResponse;
import com.nathaniel.app.mvc.util.AppResponseBuilder;

public abstract class BaseController implements EnvironmentAware {
    private static Logger       logger           = LoggerFactory.getLogger(BaseController.class);
    private static ObjectMapper objectMapper     = new ObjectMapper();
    private static String       INNER_ERROR_PAGE = "inner.error.page";
    private Environment         environment;

    /**
     * @param request http请求对象
     * @param response http响应对象
     * @param handlerMethod 发生异常的controller请求处理方法
     * @param e 具体异常
     */
    @ExceptionHandler(Exception.class)
    public void handlerExcepion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod,
                                Exception e) {
        loggException(e, handlerMethod);
        try {
            if (isJsonHttpResponse(handlerMethod)) {
                processJsonRequestException(request, response, e);
            } else if (isHtmlResponse(request)) {
                processHtmlRequestException(request, response, e);
            } else {
                processOtherMedisTypeRequestException(request, response, e);
            }
        } catch (Exception ex) {
            logger.error("处理controller异常失败:", ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    protected boolean isJsonHttpResponse(HandlerMethod handlerMethod) {
        if (handlerMethod.getBeanType().isAnnotationPresent(RequestMapping.class)) {
            return true;
        } else if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
            return true;
        } else if (handlerMethod.getMethodAnnotation(RequestMapping.class) != null) {
            return true;
        }
        return false;
    }

    protected boolean isHtmlResponse(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.ACCEPT);
        boolean isHtmlRequest = false;
        for (String accept : Splitter.on(",").splitToList(header)) {
            if (accept.equals(MediaType.TEXT_HTML_VALUE)) {
                isHtmlRequest = true;
            }
        }
        return isHtmlRequest;
    }

    protected void processJsonRequestException(HttpServletRequest request, HttpServletResponse response, Exception e)
        throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCharacterEncoding(Charset.defaultCharset().displayName());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getOutputStream().print(objectMapper.writeValueAsString(handleExeption(e)));
    }

    protected void processHtmlRequestException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        try {
            request.getRequestDispatcher(environment.getProperty(INNER_ERROR_PAGE)).forward(request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void processOtherMedisTypeRequestException(HttpServletRequest request, HttpServletResponse response,
                                                         Exception e) throws IOException {
        processHtmlRequestException(request, response, e);
    }

    private AppResponse<Void> handleExeption(Exception e) {
        if (e instanceof BusinesseException || e instanceof SystemException) {
            BaseException baseException = (BaseException) e;
            return AppResponseBuilder.errorResponse(baseException.getCode(), baseException.getMessage());
        } else {
            return AppResponseBuilder.unknowErrorResponse();
        }
    }

    private void loggException(Exception e, HandlerMethod method) {
        String controllerName = method.getBeanType().getSimpleName();
        String methodName = method.getMethod().getName();
        logger.error("{}-{}:执行失败\n{}", controllerName, methodName, e);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
