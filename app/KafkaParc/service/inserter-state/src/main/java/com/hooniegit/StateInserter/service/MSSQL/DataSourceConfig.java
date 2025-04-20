package com.hooniegit.StateInserter.service.MSSQL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "groupDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.group")
    public DataSource groupDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "groupJdbcTemplate")
    public JdbcTemplate groupJdbcTemplate(@Qualifier("groupDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}

