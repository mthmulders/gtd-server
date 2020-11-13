package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.security.User;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContextsServiceTest {
    private final ContextRepository repository = mock(ContextRepository.class);

    private final ContextsService service = new ContextsService(repository);

    @Test
    public void getAllContexts_shouldGetContextsForUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final Collection<Context> contexts = Collections.singleton(
                Fixtures.createContextForUser(user)
        );
        when(repository.findByUserId(user.getId())).thenReturn(contexts);

        // Act
        final Collection<Context> result = service.findByUserId(user.getId());

        // Assert
        assertThat(result, is(contexts));
    }

    @Test
    public void save_shouldAddUserIdToContext() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContext();

        when(repository.save(any())).thenAnswer((invocation -> invocation.getArgument(0)));

        // Act
        final Context result = service.save(user, context);

        // Assert
        assertThat(result.getUserId(), is(user.getId()));
    }

    @Test
    public void save_whenUserDoesNotOwnContext_shouldNotSave() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        when(repository.findByUserIdAndId(any(), any())).thenReturn(Optional.empty());

        // Act
        final Optional<Context> result = service.save(user, context.getId(), context);

        // Assert
        assertThat(result, isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    public void save_whenUpdatedWithOtherUserId_shouldUseUserIdFromAuthentication() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user).withId(randomUUID());
        when(repository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(context));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        final Optional<Context> result = service.save(user, context.getId(), context.withUserId(randomUUID()));

        // Assert
        assertThat(result, isPresent());
        result.ifPresent(storedContext -> assertThat(storedContext.getUserId(), is(user.getId())));
    }

    @Test
    public void findByUserIdAndId_should() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user).withId(randomUUID());
        when(repository.findByUserIdAndId(user.getId(), context.getId())).thenReturn(Optional.of(context));

        // Act
        final Optional<Context> result = service.findByUserIdAndId(user.getId(), context.getId());

        // Assert
        assertThat(result, isPresentAndIs(context));

    }
}