package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import com.infosupport.training.reactjs.gtdserver.security.User;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

public class TasksControllerTest {
    private final TaskService service = mock(TaskService.class);
    private final TasksController controller = new TasksController(service);

    @Test
    public void getAllContexts_shouldGetContextsForUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final Collection<Task> tasks = Collections.singleton(
                Fixtures.createTaskForUser(user)
        );
        when(service.findByUserId(user.getId())).thenReturn(tasks);

        // Act
        final Collection<Task> result = controller.getAllTasks(user);

        // Assert
        assertThat(result, is(tasks));
    }

    @Test
    public void createTask_shouldSaveTask() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Task task = Fixtures.createTaskForContext(context);
        when(service.save(any(), any())).then((invocation) -> invocation.getArgument(1));

        // Act
        final ResponseEntity<Task> response = controller.createTask(user, task);

        // Assert
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), is(notNullValue()));
    }

    @Test
    public void updateTask_shouldPerformUpdate() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Task task = Fixtures.createTaskForContext(context);

        when(service.save(any(), any(), any())).then((invocation) -> Optional.of(invocation.getArgument(2)));

        // Act
        final ResponseEntity<Task> response = controller.updateTask(user, context.getId(), task);

        // Assert
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), is(task));
    }
}