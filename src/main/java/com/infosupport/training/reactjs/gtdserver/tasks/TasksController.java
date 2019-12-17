package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.security.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TaskService taskService;

    @GetMapping
    public Collection<Task> getAllTasks(@AuthenticationPrincipal final User user) {
        return taskService.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@AuthenticationPrincipal final User user,
                                           @RequestBody final Task task) {
        final Task storedTask = taskService.save(user, task);
        return ResponseEntity.status(CREATED).body(storedTask);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> updateTask(@AuthenticationPrincipal final User user,
                                           @PathVariable("id") final UUID id,
                                           @RequestBody final Task task) {
        return taskService.save(user, id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
