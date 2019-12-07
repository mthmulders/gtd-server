package com.infosupport.training.reactjs.gtdserver.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a temporary class, until users are persisted in a database. Hence no tests for this one.
 */
@AllArgsConstructor
@Component
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void save(final User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            log.warn("Attempted to create user {} that already exists", existingUser.getUsername());
            throw new RuntimeException("invalid login and/or password");
        });

        final String encodedPassword = encoder.encode(user.getPassword());
        userRepository.store(user.withPassword(encodedPassword));
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}
