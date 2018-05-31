package com.nathaniel.app.core.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Properties;

public class AppLauncher {
    private static Logger logger = LoggerFactory.getLogger(AppLauncher.class);
    private static Properties contextLauncherProperties;
    private static final String LAUNCHER_CONFIG_FILE = "contextLauncher.properties";

    public static void main(String[] args) {
        prepareLaunch();
        launch(loadContextLauncherClass());
    }


    public static void launch(Class<?> contextLauncherClass) {
        ContextLauncher launcher;
        try {
            launcher = (ContextLauncher) contextLauncherClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        launcher.launchContext();
    }

    private static void prepareLaunch() {
        logger.info("begining launch app,pid:" + getProcessId());
    }

    private static Class<?> loadContextLauncherClass() {
        ClassPathResource resource = new ClassPathResource(LAUNCHER_CONFIG_FILE, AppLauncher.class);
        try {
            contextLauncherProperties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new RuntimeException("load config file: " + LAUNCHER_CONFIG_FILE + " fail!", e);
        }

        Class<?> clazz = null;
        for (String propertyName : contextLauncherProperties.stringPropertyNames()) {
            String contextLauncherClassName = contextLauncherProperties.getProperty(propertyName);
            try {
                clazz = Class.forName(contextLauncherClassName, true, AppLauncher.class.getClassLoader());
                logger.info("loaded context launcher:{}", contextLauncherClassName);
                break;
            } catch (Exception e) {
            }
        }
        return clazz;
    }

    private static String getProcessId() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String pid = runtimeMXBean.getName().split("@")[0];
        return pid;
    }


}
