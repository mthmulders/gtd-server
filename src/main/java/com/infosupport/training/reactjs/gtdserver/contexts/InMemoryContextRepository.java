package com.infosupport.training.reactjs.gtdserver.contexts;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class InMemoryContextRepository implements ContextRepository {
    private static final Collection<Context> DATA = Arrays.asList(
            Context.builder().id(0).name("Inbox").build(),
            Context.builder().id(1).name("@Work").build(),
            Context.builder().id(2).name("@Home").build(),
            Context.builder().id(3).name("@Supermarket").build()
    );

    @Override
    public Collection<Context> getAllContexts() {
        return Collections.unmodifiableCollection(DATA);
    }
}
