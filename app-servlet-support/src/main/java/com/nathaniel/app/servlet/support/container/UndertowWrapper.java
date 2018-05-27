package com.nathaniel.app.servlet.support.container;

import com.google.common.collect.Sets;
import com.nathaniel.app.servlet.support.model.UndertowConfig;
import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;

import javax.annotation.Resource;

@Component
public class UndertowWrapper {
    private static Logger logger = LoggerFactory.getLogger(UndertowWrapper.class);

    @Resource
    private UndertowConfig config;

    private Undertow undertow;


    public void startup() {
        initDeploymentInfo();
        initServer();
        undertow.start();
    }

    private void initServer() {
        Undertow.Builder builder = Undertow.builder();
        builder.setIoThreads(config.getIoThreads());
        builder.setWorkerThreads(config.getWorkThreads());
        builder.setByteBufferPool(new DefaultByteBufferPool(config.getUseDirectBuffers(), config.getBufferSize(), config.getBufferPoolSize(), 12));

        undertow = builder.build();
    }

    public void shutdown() {
        undertow.stop();
    }

    private DeploymentManager initDeploymentInfo() {
        DeploymentInfo deployment = Servlets.deployment();
        ImmediateInstanceFactory<SpringServletContainerInitializer> instanceFactory = new ImmediateInstanceFactory<>(new SpringServletContainerInitializer());
        ServletContainerInitializerInfo containerInitializerInfo = new ServletContainerInitializerInfo(SpringServletContainerInitializer.class, instanceFactory, Sets.newHashSet(WebApplicationInitializer.class));
        deployment.addServletContainerInitalizer(containerInitializerInfo);
        deployment.setContextPath("");
        deployment.setDeploymentName("");
        deployment.setDisplayName("");
        deployment.setClassLoader(Thread.currentThread().getContextClassLoader());
        return null;
    }


}
