package com.nathaniel.app.servlet.support.listener;

import com.nathaniel.app.servlet.support.container.UndertowWrapper;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InnerShutdownCompleteListener implements GracefulShutdownHandler.ShutdownListener {
    private static Logger logger = LoggerFactory.getLogger(InnerShutdownCompleteListener.class);
    private UndertowWrapper undertow;

    @Override
    public void shutdown(boolean shutdownSuccessful) {
        logger.info("all request has bean processed,begin shutdown server...");
        if (shutdownSuccessful) {
            undertow.shutdown();
        }
        logger.info("server shutdown complete!");
    }

    public void setUndertowWrapper(UndertowWrapper undertow) {
        this.undertow = undertow;
    }
}
