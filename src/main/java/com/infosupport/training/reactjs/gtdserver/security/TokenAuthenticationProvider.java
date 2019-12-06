package com.infosupport.training.reactjs.gtdserver.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This class is responsible of finding the user by its authentication token.
 */
@AllArgsConstructor
@Component
@Slf4j
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final UserAuthenticationService auth;

    @Override
    protected void additionalAuthenticationChecks(final UserDetails details,
                                                  final UsernamePasswordAuthenticationToken auth) {
        // Nothing to do
    }

    @Override
    protected UserDetails retrieveUser(final String username,
                                       final UsernamePasswordAuthenticationToken authentication) {
        final Object token = authentication.getCredentials();
        return Optional.ofNullable(token)
                .map(String::valueOf)
                .flatMap(auth::findByToken)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token " + token));
    }
}
