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

import com.nathaniel.app.mvc.WebAppContextConfigLoader;

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
