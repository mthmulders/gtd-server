package com.infosupport.training.reactjs.gtdserver;

import com.infosupport.training.reactjs.gtdserver.security.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class Fixtures {
    public static User createUser() {
        final String username = RandomStringUtils.randomAscii(4);
        final String password = RandomStringUtils.randomAscii(8);

        return User.builder().id(UUID.randomUUID()).password(password).username(username).build();
    }
}
