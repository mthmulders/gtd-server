package com.infosupport.training.reactjs.gtdserver.integration;

import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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
    public void createTasks() {
        final String token = createUser();

        final UUID contextId = UUID.fromString(
            given().
                    contentType("application/json").
                    header("Authorization", "Bearer " + token).
                    get("/contexts").
            then().
                    statusCode(200).
                    extract().
                    path("[0].id")
        );

        final Task task = Task.builder().contextId(contextId).text("Example").build();

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                body(task).
                post("/tasks").
                then().
                statusCode(200);

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
