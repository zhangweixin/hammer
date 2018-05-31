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
package com.nathaniel.app.mvc.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppServletDispatcher extends HttpServlet implements WebAppContextConfigLoader {
    Logger                    logger = LoggerFactory.getLogger(AppServletDispatcher.class);
    private DispatcherServlet dispatcherServlet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dispatcherServlet = new DispatcherServlet(createDispatchServletContext(config));
        dispatcherServlet.init(config);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        dispatcherServlet.service(req, res);
    }

    @Override
    public void destroy() {
        dispatcherServlet.destroy();
    }

    protected WebApplicationContext createDispatchServletContext(ServletConfig config) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        registerConfigClass(paramName -> config.getServletContext().getInitParameter(paramName), context);
        return context;
    }
}
