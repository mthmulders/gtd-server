package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.security.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    public Collection<Task> findByUserId(final UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task save(final User user, final Task task) {
        return taskRepository.save(task.withUserId(user.getId()));
    }

    public Optional<Task> save(final User user, final UUID id, final Task task) {
        return taskRepository
                .findByUserIdAndId(user.getId(), id)
                .map(existing -> task.withUserId(existing.getId()).withId(existing.getId()))
                .map(updated -> this.save(user, updated));
    }
}
