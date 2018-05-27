package com.nathaniel.app.servlet.support.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UndertowConfig {

    @Value("${undertow.server.port:8080}")
    private Integer port;
    @Value("${undertow.server.buffer.size:#{1024*1024*16-20}}")
    private Integer bufferSize;
    @Value("${undertow.server.buffer.pool.size:128")
    private Integer bufferPoolSize;
    @Value("${undertow.server.thread.io.size:4}")
    private Integer ioThreads;
    @Value("${undertow.server.thread.work.size:32}")
    private Integer workThreads;
    @Value("${undertow.server.direct.buffers:true}")
    private Boolean useDirectBuffers;

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
}
