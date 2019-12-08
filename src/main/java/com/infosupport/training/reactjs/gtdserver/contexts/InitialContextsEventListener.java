package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.security.UserRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class InitialContextsEventListener implements ApplicationListener<UserRegisteredEvent> {
    private static final Collection<Context> DATA = Arrays.asList(
            Context.builder().name("Inbox").build(),
            Context.builder().name("@Work").build(),
            Context.builder().name("@Home").build(),
            Context.builder().name("@Supermarket").build()
    );

    private final ContextRepository contextRepository;

    @Override
    public void onApplicationEvent(final UserRegisteredEvent event) {
        final UUID userId = ((User) event.getSource()).getId();
        final String username = ((User) event.getSource()).getUsername();
        log.info("Creating {} contexts for user {}", DATA.size(), username);
        DATA.forEach(context -> contextRepository.save(context.withUserId(userId)));
    }
}
