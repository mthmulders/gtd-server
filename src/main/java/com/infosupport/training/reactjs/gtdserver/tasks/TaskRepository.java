package com.infosupport.training.reactjs.gtdserver.tasks;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, UUID> {
    /**
     * Finds all tasks for an user.
     * @param userId The ID of the user.
     * @return All {@link Task} for the given user.
     */
    @Query("select id, context_id, user_id, text from Tasks t where t.user_id = :userId")
    Collection<Task> findByUserId(@Param("userId") UUID userId);
}
