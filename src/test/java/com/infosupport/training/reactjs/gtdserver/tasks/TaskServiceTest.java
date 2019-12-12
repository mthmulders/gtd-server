package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import com.infosupport.training.reactjs.gtdserver.security.User;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    private final TaskRepository repository = mock(TaskRepository.class);
    private final TaskService service = new TaskService(repository);

    @Test
    public void getAllContexts_shouldGetContextsForUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Collection<Task> contexts = Collections.singleton(
                Fixtures.createTaskForContext(context)
        );
        when(repository.findByUserId(user.getId())).thenReturn(contexts);

        // Act
        final Collection<Task> result = service.findByUserId(user.getId());

        // Assert
        assertThat(result, is(contexts));
    }

    @Test
    public void save_shouldAddUserIdToTask() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Task task = Fixtures.createTaskForContext(context);

        when(repository.save(any())).thenAnswer((invocation -> invocation.getArgument(0)));

        // Act
        final Task result = service.save(user, task);

        // Assert
        assertThat(result.getUserId(), is(user.getId()));
    }

    @Test
    public void save_whenUserDoesNotOwnTask_shouldNotSave() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Task task = Fixtures.createTaskForUserAndContext(user, context);
        when(repository.findByUserIdAndId(any(), any())).thenReturn(Optional.empty());

        // Act
        final Optional<Task> result = service.save(user, task.getId(), task);

        // Assert
        assertThat(result, isEmpty());
        verify(repository, never()).save(any());
    }
}