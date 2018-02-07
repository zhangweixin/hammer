package com.nathaniel.app.mvc;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebContextStarter implements ServletContextListener {

    private AnnotationConfigWebApplicationContext webApplicationContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        webApplicationContext = new AnnotationConfigWebApplicationContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        webApplicationContext.close();
    }
}