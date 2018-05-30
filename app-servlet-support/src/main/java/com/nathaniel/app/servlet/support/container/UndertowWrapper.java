package com.nathaniel.app.servlet.support.container;

import com.google.common.collect.Sets;
import com.nathaniel.app.servlet.support.listener.UndertowShutdownListener;
import com.nathaniel.app.servlet.support.model.UndertowConfig;
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
import org.springframework.beans.factory.config.BeanDefinition;
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
import java.awt.*;
import java.util.Arrays;
import java.util.Set;

@Component
public class UndertowWrapper implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(UndertowWrapper.class);

    @Resource
    private UndertowConfig config;
    @Resource
    private UndertowShutdownListener shutdownListener;
    private ConfigurableApplicationContext cac;

    private Undertow undertow;

    private DeploymentManager deploymentManager;

    private GracefulShutdownHandler gracefulShutdownHandler;

    public void startup() {
        initDeploymentManager();
        initHttpServer();
        startContainer();
    }

    private void initHttpServer() {
        Undertow.Builder builder = Undertow.builder();
        builder.setIoThreads(config.getIoThreads());
        builder.setWorkerThreads(config.getWorkThreads());
        builder.setByteBufferPool(new DefaultByteBufferPool(config.getUseDirectBuffers(), config.getBufferSize(), config.getBufferPoolSize(), 12));
        builder.addHttpListener(config.getPort(), config.getListenAddress());

        try {
            HttpHandler httpHandler = deploymentManager.start();
            gracefulShutdownHandler = new GracefulShutdownHandler(httpHandler);
            gracefulShutdownHandler.addShutdownListener(shutdownListener);
            builder.setHandler(gracefulShutdownHandler);
            undertow = builder.build();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDeploymentManager() {
        DeploymentInfo deployment = Servlets.deployment();
        ImmediateInstanceFactory<SpringServletContainerInitializer> instanceFactory = new ImmediateInstanceFactory<>(new SpringServletContainerInitializer());
        ServletContainerInitializerInfo containerInitializerInfo = new ServletContainerInitializerInfo(SpringServletContainerInitializer.class, instanceFactory, retrieveInitializer());
        deployment.addServletContainerInitalizer(containerInitializerInfo);
        deployment.setContextPath(config.getContextPath());
        deployment.setDeploymentName("");
        deployment.setDisplayName("");
        deployment.setServerName(config.getServerName());
        deployment.setClassLoader(Thread.currentThread().getContextClassLoader());

        deploymentManager = Servlets.newContainer().addDeployment(deployment);
        deploymentManager.deploy();

        SessionManager sessionManager = deploymentManager.getDeployment().getSessionManager();
        sessionManager.setDefaultSessionTimeout(config.getSessionTimeout());
    }


    private void startContainer() {
        undertow.start();
    }

    public void shutdown() {
        undertow.stop();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.cac = (ConfigurableApplicationContext) applicationContext;
        }
    }


    private Set<Class<?>> retrieveInitializer() {
        String[] names = cac.getBeanNamesForType(WebApplicationInitializer.class);
        if (names != null && names.length != 0) {
            ConfigurableListableBeanFactory beanFactory = cac.getBeanFactory();
            Set<Class<?>> beanClasses = Sets.newHashSet();
            for (String name : names) {
                AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory.getBeanDefinition(name);
                if (beanDefinition.hasBeanClass()) {
                    beanClasses.add(beanDefinition.getBeanClass());
                } else {
                    try {
                        beanClasses.add(beanDefinition.resolveBeanClass(Thread.currentThread().getContextClassLoader()));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return beanClasses;
        }
        return Sets.newHashSet();
    }



}
