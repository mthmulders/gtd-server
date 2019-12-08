package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.security.User;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContextsControllerTest {
    private static final String PASSWORD = ".`T*bu*{A~3xZ_E";
    private static final String USERNAME = "john.doe@example.com";
    private static final User USER = User.builder().id(UUID.randomUUID()).password(PASSWORD).username(USERNAME).build();

    private final ContextRepository repository = mock(ContextRepository.class);
    private final ContextsController controller = new ContextsController(repository);

    @Test
    public void getAllContexts_shouldGetContextsForUser() {
        // Arrange
        final Collection<Context> contexts = Collections.singleton(
                Context.builder().name("Example").build()
        );
        when(repository.findByUserId(USER.getId())).thenReturn(contexts);

        // Act
        final Collection<Context> result = controller.getAllContexts(USER);

        // Assert
        assertThat(result, is(contexts));
    }

    @Test
    public void save_shouldAddUserId() {
        // Arrange
        final Context context = Context.builder().name("Example").build();

        // Act
        controller.createContext(USER, context);

        // Assert
        final ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(repository).save(contextCaptor.capture());
        final Context storedContext = contextCaptor.getValue();
        assertThat(storedContext.getName(), is(context.getName()));
        assertThat(storedContext.getUserId(), is(USER.getId()));
    }
}