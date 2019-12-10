package com.infosupport.training.reactjs.gtdserver.security;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenAuthenticationServiceTest {
    private final TokenService tokens = mock(TokenService.class);
    private final UserService users = mock(UserService.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    private final TokenAuthenticationService service = new TokenAuthenticationService(tokens, users, encoder);


    @Test
    public void login_withValidUsernameAndPassword_shouldGenerateToken() {
        // Arrange
        final User user = Fixtures.createUser();
        final String token = UUID.randomUUID().toString();
        when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(tokens.expiring(any())).thenReturn(token);
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        // Act
        final Optional<String> result = service.login(user.getUsername(), user.getPassword());

        // Assert
        assertThat(result, isPresentAndIs(token));
    }

    @Test
    public void login_withInvalidUsernameAndPassword_shouldNotGenerateToken() {
        // Arrange
        final User user = Fixtures.createUser();
        when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), eq("wrong-password"))).thenReturn(false);

        // Act
        service.login(user.getUsername(), "wrong-password");

        // Assert
        verify(tokens, never()).expiring(any());
    }

    @Test
    public void findByToken_withValidToken_shouldReturnUser() {
        // Arrange
        final User user = Fixtures.createUser();
        final String token = UUID.randomUUID().toString();
        final Map<String, String> claims = Collections.singletonMap("username", user.getUsername());
        when(tokens.verify(token)).thenReturn(claims);
        when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        final Optional<User> result = service.findByToken(token);

        // Assert
        assertThat(result, isPresentAndIs(user));
    }
}