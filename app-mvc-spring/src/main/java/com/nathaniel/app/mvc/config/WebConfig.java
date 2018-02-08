package com.nathaniel.app.mvc.config;

import java.util.List;
import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springmvc配置
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
        mapperBuilder.timeZone(TimeZone.getDefault())
                     .indentOutput(true)
                     .simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        converters.add(new MappingJackson2HttpMessageConverter(mapperBuilder.build()));
    }


}
