package com.infosupport.training.reactjs.gtdserver.tasks;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class TasksController {
    private final TaskRepository taskRepository;

    @RequestMapping(value = "/tasks", method = GET)
    public Collection<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

}
