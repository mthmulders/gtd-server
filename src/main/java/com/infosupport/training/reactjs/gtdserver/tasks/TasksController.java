package com.infosupport.training.reactjs.gtdserver.tasks;

import com.infosupport.training.reactjs.gtdserver.security.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TaskRepository taskRepository;

    @GetMapping
    public Collection<Task> getAllTasks(@AuthenticationPrincipal final User user) {
        return taskRepository.findByUserId(user.getId());
    }

    @PostMapping
    public void createTask(@AuthenticationPrincipal final User user,
                           @RequestBody final Task task) {
        taskRepository.save(task.withUserId(user.getId()));
    }
}
