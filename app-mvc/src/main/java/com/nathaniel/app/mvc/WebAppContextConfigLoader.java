package com.nathaniel.app.mvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.nathaniel.app.core.util.ConfigurationLoader;
import com.nathaniel.app.mvc.servlet.AppServletDispatcher;
import com.nathaniel.app.mvc.util.WebConfigParamsWrapper;

/**
 * spring-context配置类/配置文件加载工具
 * 
 * @author nathaniel 2018-02-22
 */
public interface WebAppContextConfigLoader {
    Logger logger                     = LoggerFactory.getLogger(WebApplicationContext.class);
    String CONFIG_CLASS_FILE_DIR      = "META-INF/app/app.configuration";
    String MAIN_CONFIG_CLASS_PARAM    = "mainConfigClass";
    String CONFIG_FILE_LOCATION_PARAM = "configFileLocation";

    /**
     * 加载spring扩展配置组件
     * 
     * @return
     */
    default List<Class<?>> loadConfigClass() {
        return ConfigurationLoader.loadConfigurationClass(CONFIG_CLASS_FILE_DIR).stream().filter(it -> {
            if (it.isAnnotationPresent(Configuration.class)) {
                return true;
            } else {
                logger.warn("{}没有被{}annotation注解,忽略此配置", it.getName(), Configuration.class.getSimpleName());
                return false;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 注册配置组件类/设置配置文件到给定的annotationApplicationContext
     * 
     * @param paramsWrapper
     * @param context
     */
    default void registerConfigClass(WebConfigParamsWrapper paramsWrapper, AnnotationConfigWebApplicationContext context) {
        Optional.ofNullable(paramsWrapper.getConfigParam(MAIN_CONFIG_CLASS_PARAM)).ifPresent(mainConfigClassName -> {
            try {
                Class<?> mainConfigClass = ClassUtils.forName(mainConfigClassName, AppServletDispatcher.class.getClassLoader());
                logger.info("注册app自定义配置类:{}", mainConfigClass.getName());
                context.register(mainConfigClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        loadConfigClass().forEach(configClass -> {
            logger.info("注册annotation配置类:{}", configClass.getName());
            context.register(configClass);
        });

        Optional.ofNullable(paramsWrapper.getConfigParam(CONFIG_FILE_LOCATION_PARAM)).ifPresent(configFileLocation -> {
            logger.info("注册配置文件:{}", configFileLocation);
            context.setConfigLocation(configFileLocation);
        });
    }
}
