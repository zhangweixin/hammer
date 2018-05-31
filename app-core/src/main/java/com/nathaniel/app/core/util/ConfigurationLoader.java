/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nathaniel.app.core.util;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class ConfigurationLoader {
    private static final Logger logger         = LoggerFactory.getLogger(ConfigurationLoader.class);
    private static final String COMMENT_PREFIX = "#";

    public static Set<Class<?>> loadConfigurationClass(String classPathFile) {
        ClassLoader classLoader = findClassLoader();
        Map<String, Class<?>> clazzMap = Maps.newHashMap();
        try {
            Enumeration<URL> configUrls = classLoader.getResources(classPathFile);
            while (configUrls.hasMoreElements()) {
                CharSource charSource = Resources.asCharSource(configUrls.nextElement(), Charset.defaultCharset());
                charSource.forEachLine(line -> {
                    if (line.startsWith(COMMENT_PREFIX)){
                        return;
                    }

                    if (Strings.isNullOrEmpty(line)) {
                        return;
                    }

                    try {
                        Class<?> clazz = Class.forName(line.trim(), true, classLoader);
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
