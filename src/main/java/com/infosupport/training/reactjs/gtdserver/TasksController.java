package com.infosupport.training.reactjs.gtdserver;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
public class TasksController {
    private static Collection<Task> DATA = Arrays.asList(
            Task.builder().id(0).text("Learn React at Info Support").context_id(0).build(),
            Task.builder().id(1).text("Build my first React app").context_id(1).build(),
            Task.builder().id(2).text("Buy milk").context_id(3).build()
    );

    @RequestMapping(value = "/tasks", method = GET)
    public Collection<Task> getAllTasks() {
        return DATA;
    }

}
