package com.infosupport.training.reactjs.gtdserver.users;

import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.security.UserAuthenticationService;
import com.infosupport.training.reactjs.gtdserver.security.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/public/users")
@Slf4j
public class PublicUsersController {
    private final UserAuthenticationService authentication;
    private final UserService users;

    @PostMapping("/register")
    public String register(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password
    ) {
        final User user = User.builder()
                .username(username)
                .password(password)
                .build();

        users.save(user);
        log.info("User account {} created", username);
        return login(username, password);
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password
    ) {
        return authentication
                .login(username, password)
                .map(token -> {
                    log.info("User {} successfully logged in", username);
                    return token;
                })
                .orElseThrow(() -> {
                    log.info("Authentication failure for user {}", username);
                    return new RuntimeException("invalid login and/or password");
                });
    }
}
