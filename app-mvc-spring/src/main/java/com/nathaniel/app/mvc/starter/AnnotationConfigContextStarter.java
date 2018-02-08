package com.nathaniel.app.mvc.starter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class AnnotationConfigContextStarter extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {

        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected String getServletName() {
        return "spring-mvc-dispatcher";
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.getInitParameter("");
        super.onStartup(servletContext);
    }
}
