package com.nathaniel.app.servlet.support.config;

import com.nathaniel.app.servlet.support.container.UndertowWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpServerConfig {

    @Bean
    UndertowWrapper undertowWrapper() {
        return new UndertowWrapper();
    }

    @Bean
    UndertowConfig undertowConfig() {
        return new UndertowConfig();
    }

}
