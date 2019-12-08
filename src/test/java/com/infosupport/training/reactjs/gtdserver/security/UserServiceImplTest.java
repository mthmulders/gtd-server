package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    private static final String PASSWORD = ".`T*bu*{A~3xZ_E";
    private static final String USERNAME = "john.doe@example.com";
    private static final User USER = User.builder().password(PASSWORD).username(USERNAME).build();

    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    private final UserServiceImpl service = new UserServiceImpl(eventPublisher, userRepository, encoder);

    @Test(expected = RuntimeException.class)
    public void save_whenUserExists_shouldThrowException() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        // Act
        service.save(USER);
    }

    @Test
    public void save_shouldFireEvent() {
        // Arrange
        final UUID userId = UUID.randomUUID();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(USER.withId(userId));
        when(encoder.encode(PASSWORD)).thenReturn("");

        // Act
        service.save(USER);

        // Assert
        final ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        final UserRegisteredEvent event = eventCaptor.getValue();
        assertThat(event.getSource(), is(instanceOf(User.class)));
        assertThat(((User) event.getSource()).getId(), is(userId));
    }

    @Test
    public void save_whenUserDoesNotExists_shouldStoreWithEncodedPassword() {
        // Arrange
        final String encodedPassword = "gibberish";
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(encoder.encode(PASSWORD)).thenReturn(encodedPassword);
        when(userRepository.save(any())).thenReturn(USER.withId(UUID.randomUUID()));

        // Act
        service.save(USER);

        // Assert
        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        final User storedUser = userCaptor.getValue();
        assertThat(storedUser.getUsername(), is(USERNAME));
        assertThat(storedUser.getPassword(), is(encodedPassword));
    }

    @Test
    public void findByUsername_shouldQueryRepository() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        // Act
        final Optional<User> result = service.findByUsername(USERNAME);

        // Assert
        assertThat(result, optionalWithValue(is(USER)));
    }
}