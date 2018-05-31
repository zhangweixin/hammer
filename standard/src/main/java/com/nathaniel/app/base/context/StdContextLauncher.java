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
    private static Logger                      logger         = LoggerFactory.getLogger(StdContextLauncher.class);
    private static final String                EXT_CONFIG_DIR = "META-INF/app/app.configuration";

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
