package com.infosupport.training.reactjs.gtdserver;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ContextsIT {
    @LocalServerPort
    private Integer localPort;

    private String token;

    @Before
    public void configurePort() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = localPort;

        token = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", "john@doe.com")
                .formParam("password", "d03")
                .post("/public/users/register")
                .getBody().asString();
    }
    
    @Test
    public void retrieveContexts() {
        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/api/contexts").
        then().
                statusCode(200).
                body("$.size()", is(4));
    }
}
