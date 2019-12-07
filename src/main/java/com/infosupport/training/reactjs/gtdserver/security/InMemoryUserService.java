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
public class InMemoryUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void save(final User user) {
        final String encodedPassword = encoder.encode(user.getPassword());
        userRepository.store(user.withPassword(encodedPassword));
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}
