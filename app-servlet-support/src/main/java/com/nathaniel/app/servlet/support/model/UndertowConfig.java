package com.nathaniel.app.servlet.support.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UndertowConfig {

    @Value("${undertow.server.name:Undertow}")
    private String serverName;
    @Value("${undertow.server.port:8686}")
    private Integer port;
    @Value("${undertow.server.listen.address:0.0.0.0}")
    private String listenAddress;
    @Value("${undertow.buffer.size:#{1024*1024*16-20}}")
    private Integer bufferSize;
    @Value("${undertow.buffer.pool.size:64")
    private Integer bufferPoolSize;
    @Value("${undertow.server.thread.io.size:2}")
    private Integer ioThreads;
    @Value("${undertow.server.thread.work.size:16}")
    private Integer workThreads;
    @Value("${undertow.server.direct.buffers:true}")
    private Boolean useDirectBuffers;
    @Value("${undertow.context.session.timeout:60}")
    private Integer sessionTimeout;
    @Value("${undertow.context.path:\"\"")
    private String contextPath;
    @Value("${undertow.context.display.name:\"\"")
    private String displayName;
    @Value("${undertow.context.deploy.name:\"\"")
    private String deploymentName;


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }


    public Integer getIoThreads() {
        return ioThreads;
    }

    public void setIoThreads(Integer ioThreads) {
        this.ioThreads = ioThreads;
    }

    public Integer getWorkThreads() {
        return workThreads;
    }

    public void setWorkThreads(Integer workThreads) {
        this.workThreads = workThreads;
    }

    public Boolean getUseDirectBuffers() {
        return useDirectBuffers;
    }

    public void setUseDirectBuffers(Boolean useDirectBuffers) {
        this.useDirectBuffers = useDirectBuffers;
    }


    public Integer getBufferPoolSize() {
        return bufferPoolSize;
    }

    public void setBufferPoolSize(Integer bufferPoolSize) {
        this.bufferPoolSize = bufferPoolSize;
    }

    public String getListenAddress() {
        return listenAddress;
    }

    public void setListenAddress(String listenAddress) {
        this.listenAddress = listenAddress;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
}
