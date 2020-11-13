package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenAuthenticationFilterTest {
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    private final TokenAuthenticationFilter filter = new TokenAuthenticationFilter((request) -> true);

    @BeforeEach
    public void injectAuthenticationManager() {
        this.filter.setAuthenticationManager(authenticationManager);
    }

    @Test
    public void attemptAuthentication_withValidAuthorizationHeader_shouldReturnAuthentication() {
        // Arrange
        final Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer XXX");

        // Act
        final Authentication result = filter.attemptAuthentication(request, mock(HttpServletResponse.class));

        // Assert
        assertThat(result, is(authentication));
    }

    @Test
    public void attemptAuthentication_withoutValidAuthorizationHeader_shouldReturnAuthentication() {
        // Arrange
        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Act
        assertThrows(BadCredentialsException.class,
                () -> filter.attemptAuthentication(request, mock(HttpServletResponse.class)));
    }

    @Test
    public void attemptAuthentication_withInvalidAuthorizationHeader_shouldReturnAuthentication() {
        // Arrange
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(UsernameNotFoundException.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer XXX");

        // Act
        assertThrows(UsernameNotFoundException.class,
                () -> filter.attemptAuthentication(request, mock(HttpServletResponse.class)));
    }
}