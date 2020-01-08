package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.contexts.ContextRepository;
import com.infosupport.training.reactjs.gtdserver.security.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class TaskService {
    private final ContextRepository contextRepository;
    private final TaskRepository taskRepository;

    public Collection<Task> findByUserId(final UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    public Collection<Task> findTasksByUserAndContext(final UUID userId, final UUID contextId) {
        return taskRepository.findByUserIdAndContextId(userId, contextId);
    }

    public Task save(final User user, final Task task) {
        return taskRepository.save(task.withUserId(user.getId()));
    }

    public Optional<Task> save(final User user, final UUID id, final Task task) {
        return taskRepository
                .findByUserIdAndId(user.getId(), id)
                .map(existing -> task.withContextId(existing.getContextId()).withId(existing.getId()))
                .map(updated -> this.save(user, updated));
    }
}
