package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    private static final String PASSWORD = ".`T*bu*{A~3xZ_E";
    private static final String USERNAME = "john.doe@example.com";
    private static final User USER = User.builder().id(UUID.randomUUID().toString()).password(PASSWORD).username(USERNAME).build();

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    private final UserServiceImpl service = new UserServiceImpl(userRepository, encoder);

    @Test(expected = RuntimeException.class)
    public void save_whenUserExists_shouldThrowException() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        // Act
        service.save(USER);
    }

    @Test
    public void save_whenUserDoesNotExists_shouldStoreWithEncodedPassword() {
        // Arrange
        final String encodedPassword = "gibberish";
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(encoder.encode(PASSWORD)).thenReturn(encodedPassword);

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