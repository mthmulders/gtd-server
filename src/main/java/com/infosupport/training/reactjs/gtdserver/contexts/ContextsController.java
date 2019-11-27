package com.infosupport.training.reactjs.gtdserver.contexts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
public class ContextsController {
    private static final Collection<Context> DATA = Arrays.asList(
            Context.builder().id(0).name("Inbox").build(),
            Context.builder().id(1).name("@Work").build(),
            Context.builder().id(2).name("@Home").build(),
            Context.builder().id(3).name("@Supermarket").build()
    );

    @RequestMapping(value = "/contexts", method = GET)
    public Collection<Context> getAllContexts() {
        return DATA;
    }
}
