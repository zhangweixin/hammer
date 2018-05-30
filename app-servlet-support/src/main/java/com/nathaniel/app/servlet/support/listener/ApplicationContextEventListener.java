package com.nathaniel.app.servlet.support.listener;

import com.nathaniel.app.servlet.support.container.UndertowWrapper;
import io.undertow.Undertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApplicationContextEventListener implements SmartApplicationListener {

    private static Logger logger = LoggerFactory.getLogger(ApplicationContextEventListener.class);

    @Resource
    private UndertowWrapper undertow;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        if (ContextRefreshedEvent.class.isAssignableFrom(eventType) || ContextClosedEvent.class.isAssignableFrom(eventType)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            logger.info("startup undertow http server...");
            undertow.startup();
        } else if (event instanceof ContextClosedEvent) {
            logger.info("shutdown undertow http server...");
            undertow.shutdown();
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
