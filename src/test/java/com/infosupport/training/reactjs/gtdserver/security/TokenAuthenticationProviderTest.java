package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenAuthenticationProviderTest {
    private final UserAuthenticationService auth = mock(UserAuthenticationService.class);

    private final TokenAuthenticationProvider provider = new TokenAuthenticationProvider(auth);

    @Test
    public void retrieveUser_withValidToken_shouldReturnUserDetails() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        final String username = "john.doe@example.com";
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(token, token);
        final User user = User.builder().id(UUID.randomUUID()).password("").username(username).build();
        when(auth.findByToken(token)).thenReturn(Optional.of(user));

        // Act
        final UserDetails userDetails = provider.retrieveUser(null, authToken);

        // Assert
        assertThat(userDetails.getUsername(), is(username));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void retrieveUser_withInvalidToken_shouldNotReturnUserDetails() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(token, token);
        when(auth.findByToken(token)).thenReturn(Optional.empty());

        // Act
        provider.retrieveUser(null, authToken);
    }
}