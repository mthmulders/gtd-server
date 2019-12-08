package com.infosupport.training.reactjs.gtdserver.integration;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public abstract class AbstractIT {
    @LocalServerPort
    protected Integer localPort;

    protected String createUser() {
        final String username = RandomStringUtils.randomAscii(4);
        final String password = RandomStringUtils.randomAscii(8);
        log.info("Creating user {} with password {}", username, password);

        return given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", username)
                .formParam("password", password)
                .post("/public/users/register")
                .getBody().asString();
    }

    @Before
    public void configurePort() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = localPort;
        log.info("Testing against application running on port {}", localPort);
    }
}
