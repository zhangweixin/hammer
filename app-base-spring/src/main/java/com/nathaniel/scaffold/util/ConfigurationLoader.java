package com.nathaniel.scaffold.util;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

public class ConfigurationLoader {
    private static final Logger logger         = LoggerFactory.getLogger(ConfigurationLoader.class);
    private static final String EXT_CONFIG_DIR = "META-INF/app/app.configuration";

    public static Set<Class<?>> loadConfigurationClass() {
        ClassLoader classLoader = findClassLoader();
        Map<String, Class<?>> clazzMap = Maps.newHashMap();
        try {
            Enumeration<URL> configUrls = classLoader.getResources(EXT_CONFIG_DIR);
            while (configUrls.hasMoreElements()) {
                CharSource charSource = Resources.asCharSource(configUrls.nextElement(), Charset.defaultCharset());
                charSource.forEachLine(line -> {
                    try {
                        Class<?> clazz = Class.forName(line, true, classLoader);
                        if (clazz.isAnnotationPresent(Configuration.class)) {
                            clazzMap.put(clazz.getName(), clazz);
                        } else {
                            System.out.println(clazz.getName() + "没有被" + Configuration.class.getName() + "annotation注解,忽略配置");
                            logger.warn("{}没有被{}annotation注解,忽略此配置", clazz.getName(), Configuration.class.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Throwable e) {
            logger.error("加载组件配置失败", e);
        }
        return Sets.newHashSet(clazzMap.values());
    }

    public static ClassLoader findClassLoader() {
        return ConfigurationLoader.class.getClassLoader();
    }
}
