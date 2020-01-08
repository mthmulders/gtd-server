package com.infosupport.training.reactjs.gtdserver.integration;

import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import org.junit.Test;

import java.util.UUID;

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

    @Test
    public void getTasksForContext() {
        final String token = createUser();

        // Create a fresh context
        final Context context =
                given().
                        contentType("application/json").
                        header("Authorization", "Bearer " + token).
                        body(Context.builder().name("Example").build()).
                        post("/contexts").
                then().
                        statusCode(201).
                        extract().as(Context.class);


        // Create a fresh task in that context
        final Task task =
            given().
                    contentType("application/json").
                    header("Authorization", "Bearer " + token).
                    body(Task.builder().contextId(context.getId()).text("Example").build()).
                    post("/tasks").
            then().
                    statusCode(201).
                    extract().as(Task.class);

        // Find all tasks in the context
        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/contexts/" + context.getId() + "/tasks").
        then().
                statusCode(200).
                body("$.size()", greaterThan(0)).
                body("[0].id", is(task.getId().toString())).
                body("[0].text", is(task.getText())).
                body("[0].context_id", is(context.getId().toString()));
    }

    @Test
    public void getTasksForContext_withInvalidContext() {
        final String token = createUser();

        // Find all tasks in the context
        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/contexts/" + UUID.randomUUID().toString() + "/tasks").
                then().
                statusCode(404);
    }
}
