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

    @Before
    public void x() {
        RestAssured.port = localPort;
    }
    
    @Test
    public void whatever() {
        given().
                contentType("application/json").
                get("/api/contexts").
        then().
                statusCode(200).
                body("$.length()", is(4));
    }
}
