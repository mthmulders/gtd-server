package com.infosupport.training.reactjs.gtdserver.security;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Facade for persisting and retrieving {@link User} records.
 */
public interface UserRepository extends CrudRepository<User, UUID> {
    /**
     * Finds a user by its username.
     * @param username The username to search for.
     * @return Either a {@link User}, wrapped in an {@link Optional}, or an empty {@link Optional}.
     */
    @Query("select id, username, password from Users u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
