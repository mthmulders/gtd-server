package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.security.User;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

public class ContextsControllerTest {
    private final ContextRepository repository = mock(ContextRepository.class);
    private final ContextsController controller = new ContextsController(repository);

    @Test
    public void getAllContexts_shouldGetContextsForUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final Collection<Context> contexts = Collections.singleton(
                Fixtures.createContextForUser(user)
        );
        when(repository.findByUserId(user.getId())).thenReturn(contexts);

        // Act
        final Collection<Context> result = controller.getAllContexts(user);

        // Assert
        assertThat(result, is(contexts));
    }

    @Test
    public void createContext_shouldGenerateId() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(repository.save(any()))
                .then((invocation) -> invocation.getArgument(0, Context.class).withId(UUID.randomUUID()));

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
        when(repository.save(any())).then((invocation) -> invocation.getArgument(0));

        // Act
        final ResponseEntity<Context> response = controller.createContext(user, context);

        // Assert
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getUserId(), is(user.getId()));
    }

    @Test
    public void updateContext_shouldVerifyContextExists() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(repository.findByUserIdAndId(any(), any())).thenReturn(Optional.empty());

        // Act
        final ResponseEntity<Context> response = controller.updateContext(user, context.getId(), context);

        // Assert
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void updateContext_shouldPerformUpdate() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(repository.findByUserIdAndId(any(), any())).thenReturn(
                Optional.of(context)
        );
        when(repository.save(any())).then((invocation) -> invocation.getArgument(0));

        // Act
        final ResponseEntity<Context> response = controller.updateContext(user, context.getId(), context);

        // Assert
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getUserId(), is(user.getId()));
    }
}