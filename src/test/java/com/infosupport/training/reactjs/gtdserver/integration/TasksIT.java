package com.infosupport.training.reactjs.gtdserver.integration;

import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

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

        // Create the task
        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                body(task).
                post("/tasks").
        then().
                statusCode(201);

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

    @Test
    public void updateTask() {
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

        // Create the task
        final Task storedTask =
            given().
                    contentType("application/json").
                    header("Authorization", "Bearer " + token).
                    body(task).
                    post("/tasks").
            then().
                    statusCode(201).
                    extract().as(Task.class);

        final Task updatedTask = storedTask.withText("Another example");

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                body(updatedTask).
                post("/tasks/" + updatedTask.getId()).
        then().
                statusCode(200).
                body("text", not(is(task.getText()))).
                body("text", is(updatedTask.getText()));
    }
}
