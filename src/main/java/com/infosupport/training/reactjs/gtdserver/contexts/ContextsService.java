package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class ContextsService {
    private final ContextRepository repository;

    public Collection<Context> findByUserId(final UUID userId) {
        return repository.findByUserId(userId);
    }

    public Context save(final User user, final Context context) {
        return repository.save(context.withUserId(user.getId()));
    }

    public Optional<Context> save(final User user, final UUID id, final Context context) {
        return repository
                .findByUserIdAndId(user.getId(), id)
                .map(existing -> context.withId(existing.getId()))
                .map(updated -> this.save(user, updated));
    }
}
