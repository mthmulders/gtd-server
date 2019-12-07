package com.infosupport.training.reactjs.gtdserver.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import javax.sql.DataSource;

@AllArgsConstructor
@Configuration
@EnableJdbcRepositories("com.infosupport.training.reactjs.gtdserver")
public class DatabaseConfiguration {
    private final DataSource dataSource;

    @Bean
    public SpringLiquibase databaseSchema() {
        final SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db-changelog.xml");
        liquibase.setDataSource(dataSource);

        return liquibase;
    }
}
