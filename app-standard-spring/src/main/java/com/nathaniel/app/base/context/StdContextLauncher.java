package com.nathaniel.app.base.context;

import com.nathaniel.app.core.start.ContextLauncher;
import com.nathaniel.app.core.util.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

public class StdContextLauncher implements ContextLauncher {
    private static Logger logger = LoggerFactory.getLogger(StdContextLauncher.class);
    private static final String EXT_CONFIG_DIR = "META-INF/app/app.configuration";

    private AnnotationConfigApplicationContext applicationContext;

    public void configAndRefreshContext() {
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(applicationContext);
        ConfigurationLoader.loadConfigurationClass(EXT_CONFIG_DIR).forEach(annotatedConfigClass -> {
            logger.info("加载插件配置类:{}", annotatedConfigClass.getName());
            if (annotatedConfigClass.isAnnotationPresent(Configuration.class)) {
                beanDefinitionReader.register(annotatedConfigClass);
            } else {
                logger.warn("{}没有被{}annotation注解,忽略此配置", annotatedConfigClass.getName(), Configuration.class.getName());
            }
        });
        logger.info("加载配置完成,启动ApplicationContext...");
        applicationContext.refresh();
        applicationContext.registerShutdownHook();
    }

    public void createApplicationContext() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        applicationContext.setClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public void launchContext() {
        createApplicationContext();
        configAndRefreshContext();
    }

}
