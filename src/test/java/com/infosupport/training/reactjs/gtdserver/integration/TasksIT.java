package com.infosupport.training.reactjs.gtdserver.integration;

import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class TasksIT extends AbstractIT {
    @Test
    public void retrieveTasks() {
        final String token = createUser();

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/tasks").
                then().
                statusCode(200).
                body("$.size()", is(0));
    }

    @Test
    public void createTask() {
        final String token = createUser();

        final Context[] contexts =
            given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/contexts").
            then().
                statusCode(200).
                extract().as(Context[].class);

        assertThat(contexts.length, greaterThan(0));
        final Context context = contexts[0];
        final UUID contextId = context.getId();

        final Task task = Task.builder().contextId(contextId).text("Example").build();

        // Create the task
        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                body(task).
                post("/tasks").
        then().
                statusCode(200);

        // Verify the task is created
        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                get("/tasks").
        then().
                statusCode(200).
                body("$.size()", is(1)).
                body("[0].text", is(task.getText())).
                body("[0].context_id", is(contextId.toString()));
    }
}
