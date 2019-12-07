package com.infosupport.training.reactjs.gtdserver.tasks;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class TasksController {
    private final TaskRepository taskRepository;

    @GetMapping(value = "/tasks")
    public Collection<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

}
