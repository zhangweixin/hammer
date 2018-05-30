package com.nathaniel.app.servlet.support.listener;

import io.undertow.server.handlers.GracefulShutdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

@Component
public class UndertowShutdownListener implements GracefulShutdownHandler.ShutdownListener, ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(UndertowShutdownListener.class);
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void shutdown(boolean shutdownSuccessful) {
        logger.info("all request has bean processed,begin shutdown spring context...");
        applicationContext.close();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        }
    }
}
