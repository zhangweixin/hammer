package com.nathaniel.app.core.util;

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

public class ConfigurationLoader {
    private static final Logger logger         = LoggerFactory.getLogger(ConfigurationLoader.class);

    public static Set<Class<?>> loadConfigurationClass(String classPathFile) {
        ClassLoader classLoader = findClassLoader();
        Map<String, Class<?>> clazzMap = Maps.newHashMap();
        try {
            Enumeration<URL> configUrls = classLoader.getResources(classPathFile);
            while (configUrls.hasMoreElements()) {
                CharSource charSource = Resources.asCharSource(configUrls.nextElement(), Charset.defaultCharset());
                charSource.forEachLine(line -> {
                    try {
                        Class<?> clazz = Class.forName(line, true, classLoader);
                        clazzMap.put(clazz.getName(), clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Throwable e) {
            logger.error("从{}加载组件配置失败",classPathFile, e);
        }
        return Sets.newHashSet(clazzMap.values());
    }

    public static ClassLoader findClassLoader() {
        return ConfigurationLoader.class.getClassLoader();
    }
}
