package com.nathaniel.app.mvc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

public class ListenerWrapper {
    private static Logger logger = LoggerFactory.getLogger(ListenerWrapper.class);

    private ServletContextListener contextListener;
    private AnnotationAttributes listenerAttributes;

    public ListenerWrapper(ServletContextListener contextListener, AnnotationAttributes listenerAttributes) {
        this.contextListener = contextListener;
        this.listenerAttributes = listenerAttributes;
    }

    public void registerListener(ServletContext servletContext) {
        logger.info("register servlet listener：{}", contextListener.getClass().getName());
        servletContext.addListener(contextListener);
    }
}
