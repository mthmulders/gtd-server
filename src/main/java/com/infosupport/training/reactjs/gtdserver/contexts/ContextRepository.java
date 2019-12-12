package com.infosupport.training.reactjs.gtdserver.contexts;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ContextRepository extends CrudRepository<Context, UUID> {
    /**
     * Finds all contexts for an user.
     * @param userId The ID of the user.
     * @return All {@link Context} for the given user.
     */
    @Query("select id, user_id, name from Contexts c where c.user_id = :userId")
    Collection<Context> findByUserId(@Param("userId") UUID userId);

    /**
     * Find an existing context for an user.
     * @param userId The ID of the user.
     * @param id The ID of the context.
     * @return The context, if it exists for the current user.
     */
    @Query("select id, user_id, name from Contexts c where c.user_id = :userId and c.id = :id")
    Optional<Context> findByUserIdAndId(@Param("userId") UUID userId, @Param("id") UUID id);
}
