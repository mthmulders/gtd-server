package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import com.infosupport.training.reactjs.gtdserver.contexts.ContextRepository;
import com.infosupport.training.reactjs.gtdserver.security.User;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static java.util.Collections.singleton;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceTest {
    private final ContextRepository contextRepository = mock(ContextRepository.class);
    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final TaskService service = new TaskService(contextRepository, taskRepository);

    @Test
    public void findByUserId_shouldGetContextsForUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Collection<Task> tasks = singleton(
                Fixtures.createTaskForContext(context)
        );
        when(taskRepository.findByUserId(user.getId())).thenReturn(tasks);

        // Act
        final Collection<Task> result = service.findByUserId(user.getId());

        // Assert
        assertThat(result, is(tasks));
    }

    @Test
    public void save_shouldAddUserIdToTask() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Task task = Fixtures.createTaskForContext(context);

        when(taskRepository.save(any())).thenAnswer((invocation -> invocation.getArgument(0)));

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
        when(taskRepository.findByUserIdAndId(any(), any())).thenReturn(Optional.empty());

        // Act
        final Optional<Task> result = service.save(user, task.getId(), task);

        // Assert
        assertThat(result, isEmpty());
        verify(taskRepository, never()).save(any());
    }

    @Test
    public void save_whenUpdatedWithNonExistingContext_shouldUseOriginalContextSave() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user).withId(randomUUID());
        final Task task = Fixtures.createTaskForUserAndContext(user, context).withId(randomUUID());
        when(taskRepository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        final Optional<Task> result = service.save(user, task.getId(), task.withContextId(randomUUID()));

        // Assert
        assertThat(result, isPresent());
        result.ifPresent(storedTask -> assertThat(storedTask.getContextId(), is(task.getContextId())));
    }

    @Test
    public void findTasksByUserAndContext_shouldReturnTasksForContext() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user);
        final Task task = Fixtures.createTaskForUserAndContext(user, context);
        when(taskRepository.findByUserIdAndContextId(user.getId(), context.getId())).thenReturn(singleton(task));

        // Act
        final Collection<Task> result = service.findTasksByUserAndContext(user.getId(), context.getId());

        // Assert
        assertThat(result.size(), is(1));
        assertThat(result, hasItems(task));
    }

    @Test
    public void save_whenUpdatedWithOtherUserId_shouldUseUserIdFromAuthentication() {
        // Arrange
        final User user = Fixtures.createUser();
        final Context context = Fixtures.createContextForUser(user).withId(randomUUID());
        final Task task = Fixtures.createTaskForUserAndContext(user, context).withId(randomUUID());
        when(taskRepository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        final Optional<Task> result = service.save(user, task.getId(), task.withUserId(randomUUID()));

        // Assert
        assertThat(result, isPresent());
        result.ifPresent(storedTask -> assertThat(storedTask.getUserId(), is(user.getId())));
    }
}