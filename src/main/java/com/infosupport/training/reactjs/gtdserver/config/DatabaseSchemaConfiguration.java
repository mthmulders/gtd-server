package com.infosupport.training.reactjs.gtdserver.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@AllArgsConstructor
@Configuration
public class DatabaseSchemaConfiguration {
    private final DataSource dataSource;

    @Bean
    public SpringLiquibase databaseSchema() {
        final SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db-changelog.xml");
        liquibase.setDataSource(dataSource);

        return liquibase;
    }
}
