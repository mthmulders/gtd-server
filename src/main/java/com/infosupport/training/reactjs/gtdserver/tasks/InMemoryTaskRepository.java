package com.infosupport.training.reactjs.gtdserver.tasks;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class InMemoryTaskRepository implements TaskRepository {
    private static Collection<Task> data = Arrays.asList(
            Task.builder().id(0).text("Learn React at Info Support").contextId(0).build(),
            Task.builder().id(1).text("Build my first React app").contextId(1).build(),
            Task.builder().id(2).text("Buy milk").contextId(3).build()
    );

    @Override
    public Collection<Task> getAllTasks() {
        return Collections.unmodifiableCollection(data);
    }
}
