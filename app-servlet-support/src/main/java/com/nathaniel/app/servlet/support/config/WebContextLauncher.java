package com.nathaniel.app.servlet.support.config;

import com.nathaniel.app.core.start.ContextLauncher;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebContextLauncher implements ContextLauncher, WebAppContextConfigLoader {
    private AnnotationConfigWebApplicationContext webApplicationContext;

    @Override
    public void launchContext() {
        createRootApplicationContext();
        loadConfig();
        refreshContext();
    }

    private void createRootApplicationContext() {
        webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        webApplicationContext.setClassLoader(Thread.currentThread().getContextClassLoader());
    }

    private void loadConfig() {
        registerConfigClass(null, webApplicationContext);
    }

    private void refreshContext() {
        webApplicationContext.refresh();
    }

}
