package com.nathaniel.app.base.context;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.nathaniel.app.core.util.ConfigurationLoader;

public class AnnotationConfigContextStater {
    private static Logger       logger         = LoggerFactory.getLogger(AnnotationConfigContextStater.class);
    private static final String EXT_CONFIG_DIR = "META-INF/app/app.configuration";

    public static void startContext(Class<?>... customizedConfigClazz) {
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(getAppContext());
        ConfigurationLoader.loadConfigurationClass(EXT_CONFIG_DIR).forEach(annotatedConfigClass -> {
            logger.info("加载插件配置类:{}", annotatedConfigClass.getName());
            if (annotatedConfigClass.isAnnotationPresent(Configuration.class)) {
                beanDefinitionReader.register(annotatedConfigClass);
            } else {
                logger.warn("{}没有被{}annotation注解,忽略此配置", annotatedConfigClass.getName(), Configuration.class.getName());
            }
        });

        if (!ObjectUtils.isEmpty(customizedConfigClazz)) {
            Lists.newArrayList(customizedConfigClazz).forEach(config -> {
                logger.info("加载自定义配置类:{}", config.getName());
                getAppContext().register(config);
            });
        }
        logger.info("加载配置完成,启动ApplicationContext...");
        getAppContext().refresh();
    }

    public static AnnotationConfigApplicationContext getAppContext() {
        return AppContextHolder.applicationContext;
    }

    private static class AppContextHolder {
        private static AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    }
}
