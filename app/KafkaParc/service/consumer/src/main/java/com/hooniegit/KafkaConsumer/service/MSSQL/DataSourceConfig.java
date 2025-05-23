package com.hooniegit.KafkaConsumer.service.MSSQL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Database Source Configuration (Default)
 * - Need to Configure application.yml properties
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "tagDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tag")
    public DataSource tagDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "groupDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.group")
    public DataSource groupDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "tagJdbcTemplate")
    public JdbcTemplate tagJdbcTemplate(@Qualifier("tagDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "groupJdbcTemplate")
    public JdbcTemplate groupJdbcTemplate(@Qualifier("groupDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

}

