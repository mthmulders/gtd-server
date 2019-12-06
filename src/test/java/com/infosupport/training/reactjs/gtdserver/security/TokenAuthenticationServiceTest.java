package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenAuthenticationServiceTest {
    private static final String PASSWORD = ".`T*bu*{A~3xZ_E";
    private static final String USERNAME = "john.doe@example.com";
    private static final User USER = User.builder().id(UUID.randomUUID().toString()).password(PASSWORD).username(USERNAME).build();

    private final TokenService tokens = mock(TokenService.class);
    private final UserCrudService users = mock(UserCrudService.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    private final TokenAuthenticationService service = new TokenAuthenticationService(tokens, users, encoder);


    @Test
    public void login_withValidUsernameAndPassword_shouldGenerateToken() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        when(users.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(tokens.expiring(any())).thenReturn(token);
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        // Act
        final Optional<String> result = service.login(USERNAME, PASSWORD);

        // Assert
        assertThat(result, is(optionalWithValue(equalTo(token))));
    }

    @Test
    public void login_withInvalidUsernameAndPassword_shouldNotGenerateToken() {
        // Arrange
        when(users.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        // Act
        service.login(USERNAME, PASSWORD);

        // Assert
        verify(tokens, never()).expiring(any());
    }

    @Test
    public void findByToken_withValidToken_shouldReturnUser() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        final Map<String, String> claims = Collections.singletonMap("username", USERNAME);
        when(tokens.verify(token)).thenReturn(claims);
        when(users.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        // Act
        final Optional<User> result = service.findByToken(token);

        // Assert
        assertThat(result, is(optionalWithValue(equalTo(USER))));
    }
}