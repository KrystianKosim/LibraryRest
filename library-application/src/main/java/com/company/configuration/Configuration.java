package com.company.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class Configuration {

    /**
     * Method to execute liquibase nd create SpringLiquibase bean based on values in application.yaml file, to execute sql scripts
     *
     * @param dataSource
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.liquibase")
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setTestRollbackOnUpdate(true);
        return liquibase;
    }

    /**
     * Method to create DataSource bean based on values in application.yaml file
     *
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource liquibaseDataSource() {
        return DataSourceBuilder.create()
                .type(PGSimpleDataSource.class)
                .build();
    }

}
