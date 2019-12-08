package com.infosupport.training.reactjs.gtdserver.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This is a temporary class, until users are persisted in a database. Hence no tests for this one.
 */
@AllArgsConstructor
@Component
@Slf4j
public class UserServiceImpl implements UserService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void save(final User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            log.warn("Attempted to create user {} that already exists", existingUser.getUsername());
            throw new RuntimeException("invalid login and/or password");
        });

        final String encodedPassword = encoder.encode(user.getPassword());
        final User storedUser = userRepository.save(user.withPassword(encodedPassword));
        log.info("User account {} created", storedUser.getUsername());

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(storedUser));
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}
