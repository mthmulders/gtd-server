package com.infosupport.training.reactjs.gtdserver.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import javax.sql.DataSource;

@AllArgsConstructor
@Configuration
public class SecurityConfiguration {
    private final DataSource dataSource;

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource);
    }
}
