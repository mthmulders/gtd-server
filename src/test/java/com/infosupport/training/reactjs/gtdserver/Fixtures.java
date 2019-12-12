package com.infosupport.training.reactjs.gtdserver;

import com.infosupport.training.reactjs.gtdserver.contexts.Context;
import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class Fixtures {
    public static User createUser() {
        final String username = RandomStringUtils.randomAscii(4);
        final String password = RandomStringUtils.randomAscii(8);

        return User.builder().id(UUID.randomUUID()).password(password).username(username).build();
    }

    public static Context createContext() {
        return createContextForUser((UUID) null);
    }

    public static Context createContextForUser(final UUID userId) {
        return Context.builder().name("Example").userId(userId).build();
    }

    public static Context createContextForUser(final User user) {
        return createContextForUser(user.getId());
    }

    public static Task createTaskForUser(final UUID userId) {
        return createTaskForUserAndContext(userId, UUID.randomUUID());
    }

    public static Task createTaskForUser(final User user) {
        return createTaskForUserAndContext(user.getId(), UUID.randomUUID());
    }

    public static Task createTaskForUserAndContext(final UUID userId, final UUID contextId) {
        return Task.builder().text("Example").userId(userId).contextId(contextId).build();
    }

    public static Task createTaskForUserAndContext(final User user, final Context context) {
        return createTaskForUserAndContext(user.getId(), context.getId());
    }

    public static Task createTaskForContext(final UUID contextId) {
        return createTaskForUserAndContext(null, contextId);
    }

    public static Task createTaskForContext(final Context context) {
        return createTaskForUserAndContext(null, context.getId());
    }
}
