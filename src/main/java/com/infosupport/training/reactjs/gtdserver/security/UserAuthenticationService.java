package com.infosupport.training.reactjs.gtdserver.security;

import java.util.Optional;

public interface UserAuthenticationService {
    /**
     * Logs in with the given {@code username} and {@code password}.
     *
     * @param username Username
     * @param password Password
     * @return an {@link Optional} of a user when login succeeds
     */
    Optional<String> login(final String username, final String password);

    /**
     * Finds a user by its authentication.
     *
     * @param token user authentication key
     * @return an {@link Optional} of a user when authentication succeeds
     */
    Optional<User> findByToken(final String token);
}
