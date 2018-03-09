package com.nathaniel.app.datasource.config;

import com.nathaniel.app.datasource.model.DataSourceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource(DataSourceConfigBean configBean) {
        return null;
    }

    @Bean
    public DataSourceConfigBean dataSourceConfigBean() {
        return new DataSourceConfigBean();
    }
}
