package com.infosupport.training.reactjs.gtdserver.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Collections.singletonMap;

@AllArgsConstructor
@Component
@Slf4j
public class TokenAuthenticationService implements UserAuthenticationService {
    private final TokenService tokens;
    private final UserService users;
    private final PasswordEncoder encoder;

    @Override
    public Optional<String> login(final String username, final String password) {
        return users
                .findByUsername(username)
                .filter(passwordVerification(password))
                .map(user -> tokens.expiring(singletonMap("username", username)));
    }

    private Predicate<User> passwordVerification(final String suppliedPassword) {
        return user -> encoder.matches(suppliedPassword, user.getPassword());
    }

    @Override
    public Optional<User> findByToken(final String token) {
        return Optional
                .of(tokens.verify(token))
                .map(map -> map.get("username"))
                .flatMap(users::findByUsername);
    }
}
