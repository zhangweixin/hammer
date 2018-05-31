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
package com.nathaniel.app.servlet.support.container;

import com.google.common.collect.Sets;
import com.nathaniel.app.servlet.support.listener.InnerShutdownCompleteListener;
import com.nathaniel.app.servlet.support.config.UndertowConfig;
import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import io.undertow.server.session.SessionManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.nio.charset.Charset;
import java.util.Set;

@Component
public class UndertowWrapper implements ApplicationContextAware {
    private static Logger                  logger           = LoggerFactory.getLogger(UndertowWrapper.class);

    @Resource
    private UndertowConfig                 config;

    private InnerShutdownCompleteListener  shutdownListener = new InnerShutdownCompleteListener();

    private ConfigurableApplicationContext rootCac;

    private Undertow                       undertow;

    private DeploymentManager              deploymentManager;

    private GracefulShutdownHandler        gracefulShutdownHandler;

    public void startup() {
        deployApp();
        initHttpServer();
        startHttpserver();
    }

    private void initHttpServer() {
        Undertow.Builder builder = Undertow.builder();
        builder.setIoThreads(config.getIoThreads());
        builder.setWorkerThreads(config.getWorkThreads());
        builder.setByteBufferPool(new DefaultByteBufferPool(config.getUseDirectBuffers(), config.getBufferSize(),
            config.getBufferPoolSize(), 12));
        builder.addHttpListener(config.getPort(), config.getListenAddress());

        registerShutdownListener();
        try {
            HttpHandler httpHandler = deploymentManager.start();
            gracefulShutdownHandler = new GracefulShutdownHandler(httpHandler);
            builder.setHandler(gracefulShutdownHandler);
            undertow = builder.build();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void deployApp() {
        DeploymentInfo deployment = Servlets.deployment();
        ImmediateInstanceFactory<SpringServletContainerInitializer> instanceFactory = new ImmediateInstanceFactory<>(new SpringServletContainerInitializer());
        ServletContainerInitializerInfo containerInitializerInfo = new ServletContainerInitializerInfo(SpringServletContainerInitializer.class, instanceFactory, retrieveWebInitializer());

        deployment.addServletContainerInitalizer(containerInitializerInfo);
        deployment.setContextPath(config.getContextPath());
        deployment.setDeploymentName(config.getDeploymentName());
        deployment.setDisplayName(config.getDisplayName());
        deployment.setServerName(config.getServerName());
        deployment.setClassLoader(Thread.currentThread().getContextClassLoader());
        deployment.setDefaultRequestEncoding(Charset.defaultCharset().displayName());
        deployment.setDefaultResponseEncoding(Charset.defaultCharset().displayName());

        deploymentManager = Servlets.newContainer().addDeployment(deployment);
        deploymentManager.deploy();

        SessionManager sessionManager = deploymentManager.getDeployment().getSessionManager();
        sessionManager.setDefaultSessionTimeout(config.getSessionTimeout());
    }

    private void startHttpserver() {
        undertow.start();
    }

    public void shutdown() {
        try {
            deploymentManager.stop();
            undertow.stop();
        } catch (ServletException e) {
            throw new RuntimeException("unable to stop undertow", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.rootCac = (ConfigurableApplicationContext) applicationContext;
        }
    }

    private Set<Class<?>> retrieveWebInitializer() {
        String[] names = rootCac.getBeanNamesForType(WebApplicationInitializer.class);
        if (names != null && names.length != 0) {
            ConfigurableListableBeanFactory beanFactory = rootCac.getBeanFactory();
            Set<Class<?>> beanClasses = Sets.newHashSet();
            for (String name : names) {
                AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory.getBeanDefinition(name);
                if (beanDefinition.hasBeanClass()) {
                    beanClasses.add(beanDefinition.getBeanClass());
                } else {
                    try {
                        beanClasses
                            .add(beanDefinition.resolveBeanClass(Thread.currentThread().getContextClassLoader()));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return beanClasses;
        }
        return Sets.newHashSet();
    }

    public void shutdownGracefully() {
        gracefulShutdownHandler.shutdown();
    }

    private void registerShutdownListener() {
        shutdownListener.setUndertowWrapper(this);
        gracefulShutdownHandler.addShutdownListener(shutdownListener);
    }
}
