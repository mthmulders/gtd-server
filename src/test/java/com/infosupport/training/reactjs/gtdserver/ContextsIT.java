package com.infosupport.training.reactjs.gtdserver;

import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Before
    public void configurePort() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = localPort;
    }

    private String createUser() {
        final String username = RandomStringUtils.random(4);
        final String password = RandomStringUtils.random(8);

        return given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", username)
                .formParam("password", password)
                .post("/public/users/register")
                .getBody().asString();
    }
    
    @Test
    public void retrieveContexts() {
        final String token = createUser();

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/contexts").
        then().
                statusCode(200).
                body("$.size()", is(0));
    }

    @Test
    public void createContext() {
        final String token = createUser();
        final Context context = Context.builder().name("Example").build();

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                body(context).
                post("/contexts").
        then().
                statusCode(200);

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/contexts").
        then().
                statusCode(200).
                body("$.size()", is(1)).
                body("[0].name", is(context.getName()));
    }
}
