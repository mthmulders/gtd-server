package com.infosupport.training.reactjs.gtdserver.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    public Collection<Task> findByUserId(final UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
