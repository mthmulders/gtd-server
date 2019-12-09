package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.security.UserRegisteredEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InitialContextsEventListenerTest {
    private final ContextRepository contextRepository = mock(ContextRepository.class);

    private InitialContextsEventListener listener = new InitialContextsEventListener(contextRepository);

    @Test
    public void onApplicationEvent_shouldCreateInitialContexts() {
        // Arrange
        final User user = Fixtures.createUser();
        final UserRegisteredEvent event = new UserRegisteredEvent(user);

        // Act
        listener.onApplicationEvent(event);

        // Assert
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(contextRepository, atLeast(1)).save(contextCaptor.capture());
        contextCaptor.getAllValues().forEach(context -> assertThat(context.getUserId(), is(user.getId())));
    }
}