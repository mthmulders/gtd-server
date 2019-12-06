package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenAuthenticationFilterTest {
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    private final TokenAuthenticationFilter filter = new TokenAuthenticationFilter((request) -> true);

    @Before
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

    @Test(expected = BadCredentialsException.class)
    public void attemptAuthentication_withoutValidAuthorizationHeader_shouldReturnAuthentication() {
        // Arrange
        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Act
        filter.attemptAuthentication(request, mock(HttpServletResponse.class));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void attemptAuthentication_withInvalidAuthorizationHeader_shouldReturnAuthentication() {
        // Arrange
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(UsernameNotFoundException.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer XXX");

        // Act
        filter.attemptAuthentication(request, mock(HttpServletResponse.class));
    }
}