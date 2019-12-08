package com.infosupport.training.reactjs.gtdserver.integration;

import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class ContextsIT extends AbstractIT {
    
    @Test
    public void retrieveContexts() {
        final String token = createUser();

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/contexts").
        then().
                statusCode(200).
                body("$.size()", is(4));
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
                body("$.size()", is(5)).
                body("[4].name", is(context.getName()));
    }
}
