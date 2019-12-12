package com.infosupport.training.reactjs.gtdserver.integration;

import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThan;

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
                body("$.size()", greaterThan(0));
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
                statusCode(201);

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/contexts").
        then().
                statusCode(200).
                body("name", hasItem(context.getName()));
    }

    @Test
    public void updateContext() {
        final String token = createUser();
        final Context context = Context.builder().name("Example").build();

        final Context storedContext =
            given().
                    contentType("application/json").
                    header("Authorization", "Bearer " + token).
                    body(context).
                    post("/contexts").
            then().
                    statusCode(201).
                    extract().as(Context.class);

        final Context updatedContext = storedContext.withName("Another example");

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                body(updatedContext).
                post("/contexts/" + updatedContext.getId()).
        then().
                statusCode(200).
                body("name", not(is(context.getName()))).
                body("name", is(updatedContext.getName()));
    }
}
