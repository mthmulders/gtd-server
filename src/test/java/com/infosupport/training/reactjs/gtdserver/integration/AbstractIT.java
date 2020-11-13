package com.infosupport.training.reactjs.gtdserver.integration;

import com.infosupport.training.reactjs.gtdserver.GtdServerApplication;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = { GtdServerApplication.class },
        properties = { "environment = test" },
        webEnvironment = RANDOM_PORT
)
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

    @BeforeEach
    public void configurePort() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = localPort;
        log.info("Testing against application running on port {}", localPort);
    }
}
