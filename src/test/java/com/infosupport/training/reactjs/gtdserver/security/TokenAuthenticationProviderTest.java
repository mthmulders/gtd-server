package com.infosupport.training.reactjs.gtdserver.security;

import com.infosupport.training.reactjs.gtdserver.Fixtures;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenAuthenticationProviderTest {
    private final UserAuthenticationService auth = mock(UserAuthenticationService.class);

    private final TokenAuthenticationProvider provider = new TokenAuthenticationProvider(auth);

    @Test
    public void retrieveUser_withValidToken_shouldReturnUserDetails() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(token, token);
        final User user = Fixtures.createUser();
        when(auth.findByToken(token)).thenReturn(Optional.of(user));

        // Act
        final UserDetails userDetails = provider.retrieveUser(null, authToken);

        // Assert
        assertThat(userDetails.getUsername(), is(user.getUsername()));
    }

    @Test
    public void retrieveUser_withInvalidToken_shouldNotReturnUserDetails() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(token, token);
        when(auth.findByToken(token)).thenReturn(Optional.empty());

        // Act
        assertThrows(UsernameNotFoundException.class, () -> provider.retrieveUser(null, authToken));
    }
}