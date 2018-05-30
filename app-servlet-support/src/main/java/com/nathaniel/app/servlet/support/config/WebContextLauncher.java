package com.nathaniel.app.servlet.support.config;

import com.nathaniel.app.core.start.ContextLauncher;
import com.nathaniel.app.core.util.ConfigurationLoader;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebContextLauncher implements ContextLauncher {

    private AnnotationConfigWebApplicationContext webApplicationContext;

    @Override
    public void launchContext() {
        createRootApplicationContext();
        initConfig();
    }

    private void createRootApplicationContext() {
        webApplicationContext = new AnnotationConfigWebApplicationContext();
    }

    private void initConfig() {
        ConfigurationLoader.loadConfigurationClass(null);
    }
}
