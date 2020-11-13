package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import com.infosupport.training.reactjs.gtdserver.tasks.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ContextsControllerTest {
    private final ContextsService contextsService = mock(ContextsService.class);
    private final TaskService taskService = mock(TaskService.class);
    private final ContextsController controller = new ContextsController(contextsService, taskService);

    @Test
    public void getAllContexts_shouldGetContextsForUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final Collection<Context> contexts = singleton(
                Fixtures.createContextForUser(user)
        );
        when(contextsService.findByUserId(user.getId())).thenReturn(contexts);

        // Act
        final Collection<Context> result = controller.getAllContexts(user);

        // Assert
        assertThat(result, is(contexts));
    }

    @Test
    public void getAllTasksForContext_shouldFindTasksForUserAndContext() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Task task = Fixtures.createTaskForUserAndContext(user, context);
        when(contextsService.findByUserIdAndId(user.getId(), context.getId())).thenReturn(Optional.of(context));
        when(taskService.findTasksByUserAndContext(user.getId(), context.getId())).thenReturn(singleton(task));

        // Act
        final ResponseEntity<Collection<Task>> result = controller.getAllTasksForContext(user, context.getId());

        // Assert
        assertThat(result.getStatusCode(), is(OK));
        final Collection<Task> tasks = result.getBody();
        assertThat(tasks, hasSize(1));
        assertThat(tasks, containsInAnyOrder(task));
    }

    @Test
    public void createContext_shouldGenerateId() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(contextsService.save(any(), any()))
                .then((invocation) -> invocation.getArgument(1, Context.class).withId(UUID.randomUUID()));

        // Act
        final ResponseEntity<Context> response = controller.createContext(user, context);

        // Assert
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getId(), is(notNullValue()));
    }

    @Test
    public void createContext_shouldAddUserId() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(contextsService.save(any(), any())).then((invocation) -> invocation.getArgument(1));

        // Act
        final ResponseEntity<Context> response = controller.createContext(user, context);

        // Assert
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getUserId(), is(user.getId()));
    }

    @Test
    public void updateContext_shouldPerformUpdate() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(contextsService.save(any(), any(), any())).then((invocation) -> Optional.of(invocation.getArgument(2)));

        // Act
        final ResponseEntity<Context> response = controller.updateContext(user, context.getId(), context);

        // Assert
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getUserId(), is(user.getId()));
    }

    @Test
    public void updateContext_whenNonExisting_shouldNotPerformUpdate() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(contextsService.save(any(), any(), any())).then((invocation) -> Optional.empty());

        // Act
        final ResponseEntity<Context> response = controller.updateContext(user, context.getId(), context);

        // Assert
        assertThat(response.getStatusCode(), is(NOT_FOUND));
        assertThat(response.getBody(), is(nullValue()));
    }
}