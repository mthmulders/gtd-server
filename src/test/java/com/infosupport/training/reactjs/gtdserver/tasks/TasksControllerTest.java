package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.security.User;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TasksControllerTest {
    private final TaskService service = mock(TaskService.class);
    private final TasksController controller = new TasksController(service);

    @Test
    public void getAllContexts_shouldGetContextsForUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final Collection<Task> contexts = Collections.singleton(
                Task.builder().text("Example").build()
        );
        when(service.findByUserId(user.getId())).thenReturn(contexts);

        // Act
        final Collection<Task> result = controller.getAllTasks(user);

        // Assert
        assertThat(result, is(contexts));
    }

    @Test
    public void save_shouldAddUserId() {
        // Arrange
        final User user = Fixtures.createUser();
        final Task task = Task.builder().text("Example").build();

        // Act
        controller.createTask(user, task);

        // Assert
        final ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(service).save(taskCaptor.capture());
        final Task storedTask = taskCaptor.getValue();
        assertThat(storedTask.getText(), is(task.getText()));
        assertThat(storedTask.getUserId(), is(user.getId()));
    }
}