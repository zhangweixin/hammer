package com.nathaniel.app.mvc;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.nathaniel.app.core.util.ConfigurationLoader;
import org.springframework.web.context.WebApplicationContext;

public interface WebAppContextConfigLoader {
    Logger logger                 = LoggerFactory.getLogger(WebApplicationContext.class);
    String CONFIG_CLASS_FILE_DIR  = "META-INF/app/app.configuration";
    String MAIN_CONFIG_CLASS_PARAM = "mainConfigClass";
    String CONFIG_FILE_LOCATION_PARAM = "configFileLocation";
    default List<Class<?>> loadConfigClass() {
        return ConfigurationLoader.loadConfigurationClass(CONFIG_CLASS_FILE_DIR).stream().filter(it -> {
            if (it.isAnnotationPresent(Configuration.class)) {
                return true;
            } else {
                logger.warn("{}没有被{}annotation注解,忽略此配置", it.getName(), Configuration.class.getName());
                return false;
            }
        }).collect(Collectors.toList());
    }
}
