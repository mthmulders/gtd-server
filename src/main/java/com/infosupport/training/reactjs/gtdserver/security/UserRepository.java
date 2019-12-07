package com.infosupport.training.reactjs.gtdserver.security;

import java.util.Optional;

/**
 * Facade for persisting and retrieving {@link User} records.
 */
public interface UserRepository {
    /**
     * Store a user.
     * @param user The user to store.
     */
    void store(User user);

    /**
     * Finds a user by its username.
     * @param username The username to search for.
     * @return Either a {@link User}, wrapped in an {@link Optional}, or an empty {@link Optional}.
     */
    Optional<User> findByUsername(String username);
}
