package com.infosupport.training.reactjs.gtdserver.security;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    private final UserServiceImpl service = new UserServiceImpl(eventPublisher, userRepository, encoder);

    @Test(expected = RuntimeException.class)
    public void save_whenUserExists_shouldThrowException() {
        // Arrange
        final User user = Fixtures.createUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        service.save(user);
    }

    @Test
    public void save_shouldFireEvent() {
        // Arrange
        final User user = Fixtures.createUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(encoder.encode(user.getPassword())).thenReturn("");

        // Act
        service.save(user);

        // Assert
        final ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        final UserRegisteredEvent event = eventCaptor.getValue();
        assertThat(event.getSource(), is(instanceOf(User.class)));
        assertThat(((User) event.getSource()).getId(), is(user.getId()));
    }

    @Test
    public void save_whenUserDoesNotExists_shouldStoreWithEncodedPassword() {
        // Arrange
        final User user = Fixtures.createUser();
        final String encodedPassword = "gibberish";
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(encoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any())).thenReturn(user);

        // Act
        service.save(user);

        // Assert
        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        final User storedUser = userCaptor.getValue();
        assertThat(storedUser.getUsername(), is(user.getUsername()));
        assertThat(storedUser.getPassword(), is(encodedPassword));
    }

    @Test
    public void findByUsername_shouldQueryRepository() {
        // Arrange
        final User user = Fixtures.createUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        final Optional<User> result = service.findByUsername(user.getUsername());

        // Assert
        assertThat(result, isPresentAndIs(user));
    }
}